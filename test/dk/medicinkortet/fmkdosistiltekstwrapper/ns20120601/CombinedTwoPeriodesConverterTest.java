package dk.medicinkortet.fmkdosistiltekstwrapper.ns20120601;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import dk.medicinkortet.fmkdosistiltekstwrapper.DosageType;
import dk.medicinkortet.fmkdosistiltekstwrapper.rhino.DosisTilTekstWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.DosisTilTekstWrapperTestBase;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.DateOrDateTimeWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.DayWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.DosageWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.EveningDoseWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.MorningDoseWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.PlainDoseWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.StructureWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.StructuresWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.UnitOrUnitsWrapper;

public class CombinedTwoPeriodesConverterTest extends DosisTilTekstWrapperTestBase {
	
	@Test
	public void testCombined1() {
		// 1 dråbe i hvert øje 4 gange 1. dag, derefter 1 dråbe 2 x daglig
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnits("dråbe", "dråber"), 
				StructureWrapper.makeStructure(
					0, 
					null, 
					DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-01"), 
					DayWrapper.makeDay(
						1, 
						PlainDoseWrapper.makeDose(new BigDecimal(1)), 
						PlainDoseWrapper.makeDose(new BigDecimal(1)), 
						PlainDoseWrapper.makeDose(new BigDecimal(1)), 
						PlainDoseWrapper.makeDose(new BigDecimal(1)))),
				StructureWrapper.makeStructure(
					1,
					null, 
					DateOrDateTimeWrapper.makeDate("2011-01-02"), null, 
					DayWrapper.makeDay(
						1, 
						PlainDoseWrapper.makeDose(new BigDecimal(1)), 
						PlainDoseWrapper.makeDose(new BigDecimal(1))))));
		Assert.assertEquals(
			"DefaultMultiPeriodeLongTextConverterImpl", 
			DosisTilTekstWrapper.getLongTextConverterClassName(dosage));
		Assert.assertEquals(
			"Doseringen indeholder flere perioder:\n"+
			"\n"+
			"Doseringen foretages kun lørdag den 1. januar 2011:\n"+
			"   Doseringsforløb:\n"+
			"   Lørdag den 1. januar 2011: 1 dråbe 4 gange\n"+
			"\n"+
			"Doseringsforløbet starter søndag den 2. januar 2011 og gentages hver dag:\n"+
			"   Doseringsforløb:\n"+
			"   1 dråbe 2 gange daglig",
			DosisTilTekstWrapper.convertLongText(dosage));
			Assert.assertEquals(
					"CombinedTwoPeriodesConverterImpl", 
					DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
			Assert.assertEquals(
					"Første dag 1 dråbe 4 gange, herefter 1 dråbe 2 gange daglig", 
					DosisTilTekstWrapper.convertShortText(dosage));
			Assert.assertNull(DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue());
			Assert.assertEquals(DosageType.Combined, DosisTilTekstWrapper.getDosageType(dosage));
	}

	@Test
	public void testCombined2() {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnits("dråbe", "dråber"), 
				StructureWrapper.makeStructure(
					0, 
					null, 
					DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-03"), 
					DayWrapper.makeDay(
						1, 
						MorningDoseWrapper.makeDose(new BigDecimal(2)), 
						EveningDoseWrapper.makeDose(new BigDecimal(2))),
					DayWrapper.makeDay(
						2, 
						MorningDoseWrapper.makeDose(new BigDecimal(2)), 
						EveningDoseWrapper.makeDose(new BigDecimal(2))),
					DayWrapper.makeDay(
						3, 
						MorningDoseWrapper.makeDose(new BigDecimal(2)), 
						EveningDoseWrapper.makeDose(new BigDecimal(2)))),
				StructureWrapper.makeStructure(
					1,
					null, 
					DateOrDateTimeWrapper.makeDate("2011-01-02"), null, 
					DayWrapper.makeDay(
						1, 
						MorningDoseWrapper.makeDose(new BigDecimal(1)), 
						EveningDoseWrapper.makeDose(new BigDecimal(1))))));
		Assert.assertEquals(
			"DefaultMultiPeriodeLongTextConverterImpl", 
			DosisTilTekstWrapper.getLongTextConverterClassName(dosage));
		Assert.assertEquals(
			"Doseringen indeholder flere perioder:\n"+
			"\n"+
			"Doseringsforløbet starter lørdag den 1. januar 2011, og ophører mandag den 3. januar 2011:\n"+
			"   Doseringsforløb:\n"+
			"   Lørdag den 1. januar 2011: 2 dråber morgen + 2 dråber aften\n"+
			"   Søndag den 2. januar 2011: 2 dråber morgen + 2 dråber aften\n"+
			"   Mandag den 3. januar 2011: 2 dråber morgen + 2 dråber aften\n"+
			"\n"+
			"Doseringsforløbet starter søndag den 2. januar 2011 og gentages hver dag:\n"+
			"   Doseringsforløb:\n"+
			"   1 dråbe morgen + 1 dråbe aften",
			DosisTilTekstWrapper.convertLongText(dosage));
			Assert.assertEquals(
					"CombinedTwoPeriodesConverterImpl", 
					DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
			Assert.assertEquals(
					"2 dråber morgen og aften i 3 dage, herefter 1 dråbe morgen og aften", 
					DosisTilTekstWrapper.convertShortText(dosage));
			Assert.assertNull(DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue());
			Assert.assertEquals(DosageType.Combined, DosisTilTekstWrapper.getDosageType(dosage));
	}

	
}
