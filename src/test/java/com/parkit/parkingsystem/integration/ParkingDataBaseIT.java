package com.parkit.parkingsystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;

	@Mock
	private static InputReaderUtil inputReaderUtil;

	@BeforeAll
	private static void setUp() throws Exception {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
	}

	@BeforeEach
	private void setUpPerTest() throws Exception {
	
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("Vroum");
		dataBasePrepareService.clearDataBaseEntries();
	}

	@AfterAll
	private static void tearDown() {
	}

	/*vérification que cela indique bien les bonnes informations sur le ticket et
	 *  que cela indique bien les bonnes informations communiquées à la base de donnée.
	 *  Cela teste le ticket DAO et ses méthodes et sa relation aussi avec ParkingService.
	 *  (Voir les imports/Mocks)
	 * */
	@Test
	public void testParkingAVehicle() {

		// GIVEN
		when(inputReaderUtil.readSelection()).thenReturn(1);
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();

		// WHEN

		Ticket ticket = ticketDAO.getTicket("Vroum");
		ParkingSpot parkingSpot = ticket.getParkingSpot(); 
		//on récupère les données liées au parking

		// THEN
		Assertions.assertNotNull(ticket); //le ticket existe

		Assertions.assertNotNull(parkingSpot); //la place de parking aussi

		assertEquals("Vroum", ticket.getVehicleRegNumber());

		assertEquals(2,parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));

		assertEquals(4, parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE));

	}
	
	
	/* le ticket a été donné. Ici, on doit vérifier que le tarif généré et le temps de sortie sont 
	 * correctement renseignés dans la base de données. 
	 * On doit donc ou reprendre le test d'avant au départ, ou faire qu'on parte avec le ticket 
	 * de départ donné, puis on va faire dans tous les cas s'exécuter le processus de sortie qui édite le ticket.
	 **/

	@Test
	public void testParkingLotExit() throws Exception {

		
		
		// GIVEN
		ParkingSpot parkingSpot = new ParkingSpot(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR),ParkingType.CAR, false);
		
		/*public ParkingSpot(int number, ParkingType parkingType, boolean isAvailable), 
		 * de parkingService et où on ajoute l'intervention de la BDD...
		 */
		Ticket ticket = new Ticket();
		LocalDateTime inTime = LocalDateTime.now();
		inTime = inTime.minusHours(1);
		ticket.setInTime(inTime);
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("Vroum");
		ticketDAO.saveTicket(ticket);
		/* Là, on a réalisé le ticket que l'on obtient après avoir introduit le véhicule 
		 * le parking **/


		// WHEN
		
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		
		parkingService.processExitingVehicle();
		
		
		
		// THEN

		Assertions.assertNotNull(ticket);
		assertEquals(Math.round((ticketDAO.getTicket("Vroum").getPrice())*100.0)/100.0,Fare.CAR_RATE_PER_HOUR);
		assertNotNull(ticketDAO.getTicket("Vroum").getOutTime());
	

	}

}
