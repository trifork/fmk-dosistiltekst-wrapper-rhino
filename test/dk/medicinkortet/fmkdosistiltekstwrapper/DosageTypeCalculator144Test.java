package dk.medicinkortet.fmkdosistiltekstwrapper;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import dk.medicinkortet.fmkdosistiltekstwrapper.rhino.DosisTilTekstWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.DateOrDateTimeWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.DayWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.DosageWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.PlainDoseWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.StructureWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.StructuresWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.UnitOrUnitsWrapper;

/* Test fix of old error in dosagetypecalculator, that returned combined as soon as more than one structure was present */
public class DosageTypeCalculator144Test extends DosisTilTekstWrapperTestBase {

	@Test
	public void testTemporaryBefore144NowFixed() {
		DosageWrapper dosage = 
				DosageWrapper.makeDosage(
					StructuresWrapper.makeStructures(
						UnitOrUnitsWrapper.makeUnit("stk"),
						StructureWrapper.makeStructure(
							1, "mod smerter", 
							DateOrDateTimeWrapper.makeDate("2017-01-01"), DateOrDateTimeWrapper.makeDate("2017-02-01"),
							DayWrapper.makeDay(1,
								PlainDoseWrapper.makeDose(new BigDecimal(4)), 
								PlainDoseWrapper.makeDose(new BigDecimal(4))))
					));
		
		Assert.assertEquals(DosageType.Fixed, DosisTilTekstWrapper.getDosageType144(dosage));
	}
	
	@Test
	public void testCombined() {
		DosageWrapper dosage = 
				DosageWrapper.makeDosage(
					StructuresWrapper.makeStructures(
						UnitOrUnitsWrapper.makeUnit("stk"),
						StructureWrapper.makeStructure(
							1, "mod smerter", 
							DateOrDateTimeWrapper.makeDate("2017-01-01"), DateOrDateTimeWrapper.makeDate("2017-02-01"),
							DayWrapper.makeDay(1,
								PlainDoseWrapper.makeDose(new BigDecimal(4)), 
								PlainDoseWrapper.makeDose(new BigDecimal(4), true)))
					));
		
		Assert.assertEquals(DosageType.Combined, DosisTilTekstWrapper.getDosageType144(dosage));
	}
	
	@Test
	public void testFixedOnlyReturnsFixed() {
		DosageWrapper dosage = 
				DosageWrapper.makeDosage(
					StructuresWrapper.makeStructures(
						UnitOrUnitsWrapper.makeUnit("stk"),
						StructureWrapper.makeStructure(
							1, "mod smerter", 
							DateOrDateTimeWrapper.makeDate("2017-01-01"), DateOrDateTimeWrapper.makeDate("2017-02-01"),
							DayWrapper.makeDay(1,
								PlainDoseWrapper.makeDose(new BigDecimal(4)), 
								PlainDoseWrapper.makeDose(new BigDecimal(4))))
					));
		
		Assert.assertEquals(DosageType.Fixed, DosisTilTekstWrapper.getDosageType144(dosage));
	}
	
	
	@Test
	public void testMultipleStructuresAllFixedReturnsFixed() {
		DosageWrapper dosage = 
				DosageWrapper.makeDosage(
					StructuresWrapper.makeStructures(
						UnitOrUnitsWrapper.makeUnit("stk"),
						StructureWrapper.makeStructure(
							1, "mod smerter", 
							DateOrDateTimeWrapper.makeDate("2017-01-01"), DateOrDateTimeWrapper.makeDate("2017-01-03"),
							DayWrapper.makeDay(1,
								PlainDoseWrapper.makeDose(new BigDecimal(4)), 
								PlainDoseWrapper.makeDose(new BigDecimal(4)))),
						StructureWrapper.makeStructure(
							1, "mod smerter", 
							DateOrDateTimeWrapper.makeDate("2017-01-04"), null,
							DayWrapper.makeDay(1,
								PlainDoseWrapper.makeDose(new BigDecimal(4)), 
								PlainDoseWrapper.makeDose(new BigDecimal(4))))
					));
		
		Assert.assertEquals(DosageType.Fixed, DosisTilTekstWrapper.getDosageType144(dosage));
		
	}
	
	@Test
	public void testMultipleStructuresAllPNReturnsPN() {
		DosageWrapper dosage = 
				DosageWrapper.makeDosage(
					StructuresWrapper.makeStructures(
						UnitOrUnitsWrapper.makeUnit("stk"),
						StructureWrapper.makeStructure(
							1, "mod smerter", 
							DateOrDateTimeWrapper.makeDate("2017-01-01"), DateOrDateTimeWrapper.makeDate("2017-01-03"),
							DayWrapper.makeDay(1,
								PlainDoseWrapper.makeDose(new BigDecimal(4), true), 
								PlainDoseWrapper.makeDose(new BigDecimal(4), true))),
						StructureWrapper.makeStructure(
							1, "mod smerter", 
							DateOrDateTimeWrapper.makeDate("2017-01-04"), null,
							DayWrapper.makeDay(1,
								PlainDoseWrapper.makeDose(new BigDecimal(4), true), 
								PlainDoseWrapper.makeDose(new BigDecimal(4), true)))
					));
		
		Assert.assertEquals(DosageType.AccordingToNeed, DosisTilTekstWrapper.getDosageType144(dosage));
		
	}
	
	@Test
	public void testMultipleStructuresInternallyCombinedReturnsCombined() {
		DosageWrapper dosage = 
			DosageWrapper.makeDosage(
				StructuresWrapper.makeStructures(
					UnitOrUnitsWrapper.makeUnit("stk"),
					StructureWrapper.makeStructure(
						1, "mod smerter", 
						DateOrDateTimeWrapper.makeDate("2017-01-01"), null,
						DayWrapper.makeDay(1,
							PlainDoseWrapper.makeDose(new BigDecimal(4)), 
							PlainDoseWrapper.makeDose(new BigDecimal(4), true))),
					StructureWrapper.makeStructure(
						1, "mod smerter", 
						DateOrDateTimeWrapper.makeDate("2017-01-01"), null,
						DayWrapper.makeDay(1,
							PlainDoseWrapper.makeDose(new BigDecimal(4), true), 
							PlainDoseWrapper.makeDose(new BigDecimal(4))))
				));
		
		Assert.assertEquals(DosageType.Combined, DosisTilTekstWrapper.getDosageType144(dosage));
		
	}
	
	@Test
	public void testNotOverlappingFixedAndEmptyReturnsFixed() {
		DosageWrapper dosage = 
			DosageWrapper.makeDosage(
				StructuresWrapper.makeStructures(
					UnitOrUnitsWrapper.makeUnit("stk"),
					StructureWrapper.makeStructure(
						1, "mod smerter", 
						DateOrDateTimeWrapper.makeDate("2017-02-01"), DateOrDateTimeWrapper.makeDate("2017-02-02"),
						DayWrapper.makeDay(1,
							PlainDoseWrapper.makeDose(new BigDecimal(4)), 
							PlainDoseWrapper.makeDose(new BigDecimal(4), false))),
					StructureWrapper.makeStructure(
						1, "mod smerter", 
						DateOrDateTimeWrapper.makeDate("2017-02-03"), DateOrDateTimeWrapper.makeDate("2017-02-04"))
				));

		Assert.assertEquals(DosageType.Fixed, DosisTilTekstWrapper.getDosageType144(dosage));
	}


	@Test
	public void testOverlappingFixedAndEmptyReturnsCombined() {
		DosageWrapper dosage = 
			DosageWrapper.makeDosage(
				StructuresWrapper.makeStructures(
					UnitOrUnitsWrapper.makeUnit("stk"),
					StructureWrapper.makeStructure(
						1, "mod smerter", 
						DateOrDateTimeWrapper.makeDate("2017-02-01"), DateOrDateTimeWrapper.makeDate("2017-02-02"),
						DayWrapper.makeDay(1,
							PlainDoseWrapper.makeDose(new BigDecimal(4)), 
							PlainDoseWrapper.makeDose(new BigDecimal(4), false))),
					StructureWrapper.makeStructure(
						1, "mod smerter", 
						DateOrDateTimeWrapper.makeDate("2017-02-01"), DateOrDateTimeWrapper.makeDate("2017-02-02"))
				));
		
		Assert.assertEquals(DosageType.Combined, DosisTilTekstWrapper.getDosageType144(dosage));
	}

	@Test
	public void testOverlappingFixedWithoutEnddateAndEmptyWithoutEnddateReturnsCombined() {
		DosageWrapper dosage = 
			DosageWrapper.makeDosage(
				StructuresWrapper.makeStructures(
					UnitOrUnitsWrapper.makeUnit("stk"),
					StructureWrapper.makeStructure(	
						1, "mod smerter", 
						DateOrDateTimeWrapper.makeDate("2017-01-01"), null,
						DayWrapper.makeDay(1,
							PlainDoseWrapper.makeDose(new BigDecimal(4)), 
							PlainDoseWrapper.makeDose(new BigDecimal(4), false))),
					StructureWrapper.makeStructure(
						1, "mod smerter", 
						DateOrDateTimeWrapper.makeDate("2017-01-01"), null)
				));
		
		Assert.assertEquals(DosageType.Combined, DosisTilTekstWrapper.getDosageType144(dosage));
	}

	
	
	@Test
	public void testNotOverlappingPNAndEmptyReturnsPN() {
		DosageWrapper dosage = 
			DosageWrapper.makeDosage(
				StructuresWrapper.makeStructures(
					UnitOrUnitsWrapper.makeUnit("stk"),
					StructureWrapper.makeStructure(
						1, "mod smerter", 
						DateOrDateTimeWrapper.makeDate("2017-02-01"), DateOrDateTimeWrapper.makeDate("2017-02-02"),
						DayWrapper.makeDay(1,
							PlainDoseWrapper.makeDose(new BigDecimal(4), true))),
					StructureWrapper.makeStructure(
						1, "mod smerter", 
						DateOrDateTimeWrapper.makeDate("2017-02-03"), DateOrDateTimeWrapper.makeDate("2017-02-04"))
				));
	
		Assert.assertEquals(DosageType.AccordingToNeed, DosisTilTekstWrapper.getDosageType144(dosage));
	}

	@Test
	public void testOverlappingPNAndEmptyReturnsCombined() {
		DosageWrapper dosage = 
			DosageWrapper.makeDosage(
				StructuresWrapper.makeStructures(
					UnitOrUnitsWrapper.makeUnit("stk"),
					StructureWrapper.makeStructure(
						1, "mod smerter", 
						DateOrDateTimeWrapper.makeDate("2017-01-01"), null,
						DayWrapper.makeDay(1,
							PlainDoseWrapper.makeDose(new BigDecimal(4), true))),
					StructureWrapper.makeStructure(
						1, "mod smerter", 
						DateOrDateTimeWrapper.makeDate("2017-01-01"), null)
				));
		
		Assert.assertEquals(DosageType.Combined, DosisTilTekstWrapper.getDosageType144(dosage));
	}
	
	// FMK-3247 Forkerte doseringstyper ved tomme doseringsperioder
	@Test
	public void testWithSeveralEmptyPeriodsReturnsCombined() {
		DosageWrapper dosage = 
			DosageWrapper.makeDosage(
				StructuresWrapper.makeStructures(
					UnitOrUnitsWrapper.makeUnit("stk"),
					StructureWrapper.makeStructure(1, "mod smerter", 
						DateOrDateTimeWrapper.makeDate("2017-01-01"), DateOrDateTimeWrapper.makeDate("2017-01-03"),
						DayWrapper.makeDay(1,PlainDoseWrapper.makeDose(new BigDecimal(4)))),
					StructureWrapper.makeStructure(1, "mod smerter", 
						DateOrDateTimeWrapper.makeDate("2017-01-04"), DateOrDateTimeWrapper.makeDate("2017-01-12")),
					StructureWrapper.makeStructure(1, "mod smerter", 
						DateOrDateTimeWrapper.makeDate("2017-01-13"), DateOrDateTimeWrapper.makeDate("2017-01-15"),
						DayWrapper.makeDay(1,PlainDoseWrapper.makeDose(new BigDecimal(4)))),
					StructureWrapper.makeStructure(1, "mod smerter", 
							DateOrDateTimeWrapper.makeDate("2017-01-01"), DateOrDateTimeWrapper.makeDate("2017-01-15"))
				));
		
		Assert.assertEquals(DosageType.Combined, DosisTilTekstWrapper.getDosageType144(dosage));
	}
	


}
