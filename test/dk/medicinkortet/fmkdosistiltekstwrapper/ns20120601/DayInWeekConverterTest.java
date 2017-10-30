package dk.medicinkortet.fmkdosistiltekstwrapper.ns20120601;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import dk.medicinkortet.fmkdosistiltekstwrapper.rhino.DosisTilTekstWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.DosisTilTekstWrapperTestBase;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.DateOrDateTimeWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.DayWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.DosageWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.MorningDoseWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.StructureWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.StructuresWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.UnitOrUnitsWrapper;

public class DayInWeekConverterTest extends DosisTilTekstWrapperTestBase {
	
	// FMK-3273
	@Test
	public void testSupplText() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnits("tablet", "tabletter"), 
				StructureWrapper.makeStructure(
					14, 
					"ved måltid", 
					DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-30"), 
					DayWrapper.makeDay(
						3, MorningDoseWrapper.makeDose(new BigDecimal(1))),
					DayWrapper.makeDay(
						10, MorningDoseWrapper.makeDose(new BigDecimal(1))))
				));
		
		Assert.assertEquals(
				"DayInWeekConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"1 tablet morgen daglig mandag ved måltid", 
				DosisTilTekstWrapper.convertShortText(dosage));
		
		Assert.assertEquals(
				"DefaultLongTextConverterImpl", 
				DosisTilTekstWrapper.getLongTextConverterClassName(dosage));
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011, forløbet gentages efter 14 dage, og ophører søndag den 30. januar 2011.\nBemærk at doseringen har et komplekst forløb:\n"+
				"   Doseringsforløb:\n"+
				"   Mandag den 3. januar 2011: 1 tablet morgen ved måltid\n"+
				"   Mandag den 10. januar 2011: 1 tablet morgen ved måltid",
				DosisTilTekstWrapper.convertLongText(dosage));
	}
	
	// FMK-3273
		@Test
		public void testSupplTextWithPause() throws Exception {
			DosageWrapper dosage = DosageWrapper.makeDosage(
				StructuresWrapper.makeStructures(
					UnitOrUnitsWrapper.makeUnits("tablet", "tabletter"), 
					StructureWrapper.makeStructure(
						14, 
						"ved måltid", 
						DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-30"), 
						DayWrapper.makeDay(
							3, MorningDoseWrapper.makeDose(new BigDecimal(1))),
						DayWrapper.makeDay(
							10, MorningDoseWrapper.makeDose(new BigDecimal(1))))
					));
			
			Assert.assertEquals(
					"DayInWeekConverterImpl", 
					DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
			Assert.assertEquals(
					"1 tablet morgen daglig mandag ved måltid", 
					DosisTilTekstWrapper.convertShortText(dosage));
			
			Assert.assertEquals(
					"DefaultLongTextConverterImpl", 
					DosisTilTekstWrapper.getLongTextConverterClassName(dosage));
			Assert.assertEquals(
					"Doseringsforløbet starter lørdag den 1. januar 2011, forløbet gentages efter 14 dage, og ophører søndag den 30. januar 2011.\nBemærk at doseringen har et komplekst forløb:\n"+
					"   Doseringsforløb:\n"+
					"   Mandag den 3. januar 2011: 1 tablet morgen ved måltid\n"+
					"   Mandag den 10. januar 2011: 1 tablet morgen ved måltid",
					DosisTilTekstWrapper.convertLongText(dosage));
		}

}
