package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

public class FareCalculatorServiceTest {

	private static FareCalculatorService fareCalculatorService;
	private Ticket ticket;

	@BeforeAll
	private static void setUp() {
		fareCalculatorService = new FareCalculatorService();
	}

	@BeforeEach
	private void setUpPerTest() {
		ticket = new Ticket();
	}

	/*Vérification que le prix payé correspond à celui attendu.
	 * Pour une voiture.
	 */

	@Test
	public void calculateFareCar() {

		boolean loyalty = false;

		LocalDateTime inTime = LocalDateTime.now();
		inTime = inTime.minusHours(1);
		LocalDateTime outTime = LocalDateTime.now();

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, loyalty);
		assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
	}

	/* idem mais pour les vélos **/

	@Test
	public void calculateFareBike() {

		boolean loyalty = false;

		LocalDateTime inTime = LocalDateTime.now();
		inTime = inTime.minusHours(1);
		LocalDateTime outTime = LocalDateTime.now();

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, loyalty);
		assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
	}

	/* idem pour un dont on ne sait si voiture ou vélo **/

	@Test
	public void calculateFareUnkownType() {

		boolean loyalty = false;

		LocalDateTime inTime = LocalDateTime.now();
		inTime = inTime.minusHours(1);
		LocalDateTime outTime = LocalDateTime.now();

		ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket, loyalty));
	}

	/*
	 * On vérifie que si le temps de sortie est inférieur au temps d'entrée,
	 * cela va l'indiquer.
	 * 
	 */

	@Test
	public void calculateFareBikeWithFutureInTime() {

		boolean loyalty = false;

		LocalDateTime inTime = LocalDateTime.now();
		inTime = inTime.plusHours(1);
		LocalDateTime outTime = LocalDateTime.now();

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket, loyalty));
	}

	/*
	 * On recule l'instant d'entrée de 45' pour avoir une durée de stationnement de 45'.
	 * On vérifie encore que le prix correspond.
	 */

	@Test
	public void calculateFareBikeWithLessThanOneHourParkingTime() {

		boolean loyalty = false;

		LocalDateTime inTime = LocalDateTime.now();
		inTime = inTime.minusMinutes(45);
		LocalDateTime outTime = LocalDateTime.now();

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		fareCalculatorService.calculateFare(ticket, loyalty);
		assertEquals(ticket.getPrice(), (0.75 * Fare.BIKE_RATE_PER_HOUR));
	}

	@Test
	public void calculateFareCarWithLessThanOneHourParkingTime() {

		boolean loyalty = false;

		LocalDateTime inTime = LocalDateTime.now();
		inTime = inTime.minusMinutes(45);
		LocalDateTime outTime = LocalDateTime.now();

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		fareCalculatorService.calculateFare(ticket, loyalty);

		assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	/*
	 * Là on contrôle le prix pour une durée supérieure à un jour.
	 */
	@Test
	public void calculateFareCarWithMoreThanADayParkingTime() {

		boolean loyalty = false;

		LocalDateTime inTime = LocalDateTime.now();
		inTime = inTime.minusDays(1);
		LocalDateTime outTime = LocalDateTime.now();

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		fareCalculatorService.calculateFare(ticket, loyalty);

		assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareCarWithMoreThanAYearParkingTime() {

		boolean loyalty = false;

		LocalDateTime inTime = LocalDateTime.now();
		inTime = inTime.minusYears(1);
		LocalDateTime outTime = LocalDateTime.now();

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		fareCalculatorService.calculateFare(ticket, loyalty);

		assertEquals((8760 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	/*
	 * Comme on a accordé la gratuité des 30 premières minutes de stationnement, on
	 * crée là encore un recul de 30 minutes du moment d'entrée et on compare le
	 * prix payé à ce qu'il devrait être (gratuit).
	 */

	@Test
	public void calculateForGiveTheThirtyFirstMinuteForCarTest() {

		boolean loyalty = false;

		LocalDateTime inTime = LocalDateTime.now();
		inTime = inTime.minusMinutes(30);
		LocalDateTime outTime = LocalDateTime.now();

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		fareCalculatorService.calculateFare(ticket, loyalty);

		assertEquals((0 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());

	}

	@Test
	public void calculateForGiveTheThirtyFirstMinuteForBikeTest() {

		boolean loyalty = false;
		LocalDateTime inTime = LocalDateTime.now();
		inTime = inTime.minusMinutes(30);
		LocalDateTime outTime = LocalDateTime.now();

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		fareCalculatorService.calculateFare(ticket, loyalty);

		assertEquals((0 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());

	}

	/*
	 * Ici on va vérifier le problème des années bissextiles et non bissextiles.
	 * Vérifier que le système de mesure de datage/horaire le prenne bien en compte
	 * et ne facture que le temps réellement passée, que ce soit ou non une année de
	 * 28 jours ou non en février. Donc ici, on lui fait croire qu'on est
	 * précisément à une date donnée en entrée, à ce niveau charnière de fin
	 * février/début mars. Et on vérifie qu'il sait bien la réelle durée
	 * concernée/prix toujours.
	 */

	@Test
	public void calculateDuringANonLeapYearTest() {

		boolean loyalty = false;

		LocalDateTime inTime = LocalDateTime.of(2022, 2, 28, 07, 30, 00, 0000);
		LocalDateTime outTime = LocalDateTime.of(2022, 3, 01, 07, 30, 00, 0000);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, loyalty);
		assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateDuringALeapYearTest() {

		boolean loyalty = false;
		LocalDateTime inTime = LocalDateTime.of(2020, 2, 28, 07, 30, 00, 0000);
		LocalDateTime outTime = LocalDateTime.of(2020, 3, 01, 07, 30, 00, 0000);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, loyalty);
		assertEquals((48 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateDuringALeapYearBikeTest() {

		boolean loyalty = false;
		LocalDateTime inTime = LocalDateTime.of(2020, 2, 28, 07, 30, 00, 0000);
		LocalDateTime outTime = LocalDateTime.of(2020, 3, 01, 07, 30, 00, 0000);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, loyalty);
		assertEquals((48 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	/*
	 * On vérifie que la réduction accordée aux clients fidéles est bien appliquée.
	 */
	@Test
	public void calculateWithLoyaltyCarTest() {

		boolean loyalty = true;

		LocalDateTime inTime = LocalDateTime.now();
		inTime = inTime.minusHours(1);
		LocalDateTime outTime = LocalDateTime.now();

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		fareCalculatorService.calculateFare(ticket, loyalty);

		assertEquals(ticket.getPrice(), (0.95) * Fare.CAR_RATE_PER_HOUR);

	}

	@Test
	public void calculateWithLoyaltyBikeTest() {

		boolean loyalty = true;

		LocalDateTime inTime = LocalDateTime.now();
		inTime = inTime.minusHours(1);
		LocalDateTime outTime = LocalDateTime.now();

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		fareCalculatorService.calculateFare(ticket, loyalty);

		assertEquals(ticket.getPrice(), (0.95) * Fare.BIKE_RATE_PER_HOUR);

	}

	/*
	 * Changement d'heure hiver/été.
	 */
	@Test
	public void calculateWithSummerWinterTimeChangeTest() {

		boolean loyalty = false;
		LocalDateTime inTime = LocalDateTime.of(2022, 10, 29, 23, 00, 00, 0000);
		LocalDateTime outTime = LocalDateTime.of(2022, 10, 30, 01, 00, 00, 0000);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, loyalty);
		assertEquals((2 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());

	}

	@Test
	public void calculateWithSummerWinterTimeChangeBikeTest() {

		boolean loyalty = false;
		LocalDateTime inTime = LocalDateTime.of(2022, 10, 29, 23, 00, 00, 0000);
		LocalDateTime outTime = LocalDateTime.of(2022, 10, 30, 01, 00, 00, 0000);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket, loyalty);
		assertEquals((2 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());

	}

}
