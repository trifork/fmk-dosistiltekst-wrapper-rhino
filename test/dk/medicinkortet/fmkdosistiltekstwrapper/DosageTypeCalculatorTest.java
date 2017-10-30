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

/* Test fix of error (FMK-2421) in dosagetypecalculator, that returned combined as soon as more than one structure was present */
public class DosageTypeCalculatorTest extends DosisTilTekstWrapperTestBase {

	@Test
	public void testTemporary() {
		DosageWrapper dosage = 
				DosageWrapper.makeDosage(
					StructuresWrapper.makeStructures(
						UnitOrUnitsWrapper.makeUnit("stk"),
						StructureWrapper.makeStructure(
							1, "mod smerter", 
							DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-02-01"),
							DayWrapper.makeDay(1,
								PlainDoseWrapper.makeDose(new BigDecimal(4)), 
								PlainDoseWrapper.makeDose(new BigDecimal(4))))
					));
		
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));
		
	}
	
	@Test
	public void testMultipleStructuresAllFixed() {
		DosageWrapper dosage = 
				DosageWrapper.makeDosage(
					StructuresWrapper.makeStructures(
						UnitOrUnitsWrapper.makeUnit("stk"),
						StructureWrapper.makeStructure(
							1, "mod smerter", 
							DateOrDateTimeWrapper.makeDate("2011-01-01"), null,
							DayWrapper.makeDay(1,
								PlainDoseWrapper.makeDose(new BigDecimal(4)), 
								PlainDoseWrapper.makeDose(new BigDecimal(4)))),
						StructureWrapper.makeStructure(
							1, "mod smerter", 
							DateOrDateTimeWrapper.makeDate("2011-01-01"), null,
							DayWrapper.makeDay(1,
								PlainDoseWrapper.makeDose(new BigDecimal(4)), 
								PlainDoseWrapper.makeDose(new BigDecimal(4))))
					));
		
		Assert.assertEquals(DosageType.Fixed, DosisTilTekstWrapper.getDosageType(dosage));
		
	}
	
	@Test
	public void testMultipleStructuresAllPN() {
		DosageWrapper dosage = 
				DosageWrapper.makeDosage(
					StructuresWrapper.makeStructures(
						UnitOrUnitsWrapper.makeUnit("stk"),
						StructureWrapper.makeStructure(
							1, "mod smerter", 
							DateOrDateTimeWrapper.makeDate("2011-01-01"), null,
							DayWrapper.makeDay(1,
								PlainDoseWrapper.makeDose(new BigDecimal(4), true), 
								PlainDoseWrapper.makeDose(new BigDecimal(4), true))),
						StructureWrapper.makeStructure(
							1, "mod smerter", 
							DateOrDateTimeWrapper.makeDate("2011-01-01"), null,
							DayWrapper.makeDay(1,
								PlainDoseWrapper.makeDose(new BigDecimal(4), true), 
								PlainDoseWrapper.makeDose(new BigDecimal(4), true)))
					));
		
		Assert.assertEquals(DosageType.AccordingToNeed, DosisTilTekstWrapper.getDosageType(dosage));
		
	}
	
	@Test
	public void testMultipleStructuresCombined() {
		DosageWrapper dosage = 
				DosageWrapper.makeDosage(
					StructuresWrapper.makeStructures(
						UnitOrUnitsWrapper.makeUnit("stk"),
						StructureWrapper.makeStructure(
							1, "mod smerter", 
							DateOrDateTimeWrapper.makeDate("2011-01-01"), null,
							DayWrapper.makeDay(1,
								PlainDoseWrapper.makeDose(new BigDecimal(4)), 
								PlainDoseWrapper.makeDose(new BigDecimal(4), true))),
						StructureWrapper.makeStructure(
							1, "mod smerter", 
							DateOrDateTimeWrapper.makeDate("2011-01-01"), null,
							DayWrapper.makeDay(1,
								PlainDoseWrapper.makeDose(new BigDecimal(4), true), 
								PlainDoseWrapper.makeDose(new BigDecimal(4))))
					));
		
		Assert.assertEquals(DosageType.Combined, DosisTilTekstWrapper.getDosageType(dosage));
		
	}
}
