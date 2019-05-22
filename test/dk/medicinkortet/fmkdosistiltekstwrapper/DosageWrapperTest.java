/**
* The contents of this file are subject to the Mozilla Public
* License Version 1.1 (the "License"); you may not use this file
* except in compliance with the License. You may obtain a copy of
* the License at http://www.mozilla.org/MPL/
*
* Software distributed under the License is distributed on an "AS
* IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
* implied. See the License for the specific language governing
* rights and limitations under the License.
*
* Contributor(s): Contributors are attributed in the source code
* where applicable.
*
* The Original Code is "Dosis-til-tekst".
*
* The Initial Developer of the Original Code is Trifork Public A/S.
*
* Portions created for the FMK Project are Copyright 2011,
* National Board of e-Health (NSI). All Rights Reserved.
*/

package dk.medicinkortet.fmkdosistiltekstwrapper;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import dk.medicinkortet.fmkdosistiltekstwrapper.rhino.DosisTilTekstWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.DateOrDateTimeWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.DayWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.DosageWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.EveningDoseWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.FreeTextWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.MorningDoseWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.NightDoseWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.NoonDoseWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.PlainDoseWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.StructureWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.StructuresWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.UnitOrUnitsWrapper;

public class DosageWrapperTest extends DosisTilTekstWrapperTestBase {
	
	@Test
	public void testDaglig4StkModSmerter2Gange() throws Exception {
		DosageWrapper dosage = 
			DosageWrapper.makeDosage(
				StructuresWrapper.makeStructures(
					UnitOrUnitsWrapper.makeUnit("stk"),
					StructureWrapper.makeStructure(
						1, "mod smerter", 
						DateOrDateTimeWrapper.makeDate("2011-01-01"), null,
						DayWrapper.makeDay(1,
							PlainDoseWrapper.makeDose(new BigDecimal(4)), 
							PlainDoseWrapper.makeDose(new BigDecimal(4))))));
		
	
		
		DosageTranslationCombined combined = DosisTilTekstWrapper.convertCombined(dosage);
		Assert.assertEquals("4 stk 2 gange daglig mod smerter", combined.getCombinedTranslation().getShortText());
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011 og gentages hver dag:\n"+
				"   Doseringsforløb:\n"+
				"   4 stk 2 gange daglig.\n   Bemærk: mod smerter", combined.getCombinedTranslation().getLongText());
		
		String shortText = DosisTilTekstWrapper.convertShortText(dosage);
		Assert.assertEquals("4 stk 2 gange daglig.\n   Bemærk: mod smerter", shortText);
		
		Assert.assertEquals(
			"Doseringsforløbet starter lørdag den 1. januar 2011 og gentages hver dag:\n"+
			"   Doseringsforløb:\n"+
			"   4 stk 2 gange daglig.\n   Bemærk: mod smerter", 
			DosisTilTekstWrapper.convertLongText(dosage));
	}
	
	// FMK-4603
	@Test
	public void testCombinedMedForLangKortTekst() throws Exception {
		DosageWrapper dosage = 
			DosageWrapper.makeDosage(
				StructuresWrapper.makeStructures(
					UnitOrUnitsWrapper.makeUnit("stk"),
					StructureWrapper.makeStructure(
						1, "mod smerter og så en meeeeeeget lang supplerende tekst der får længden på den korte tekst til at blive længere end max-længden", 
						DateOrDateTimeWrapper.makeDate("2011-01-01"), null,
						DayWrapper.makeDay(1,
							PlainDoseWrapper.makeDose(new BigDecimal(4)), 
							PlainDoseWrapper.makeDose(new BigDecimal(4))))));
		
	
		
		DosageTranslationCombined combined = DosisTilTekstWrapper.convertCombined(dosage); 
		Assert.assertNull(combined.getCombinedTranslation().getShortText());
		Assert.assertNull(combined.getPeriodTranslations().get(0).getShortText());
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011 og gentages hver dag:\n"+
				"   Doseringsforløb:\n"+
				"   4 stk 2 gange daglig.\n   Bemærk: mod smerter og så en meeeeeeget lang supplerende tekst der får længden på den korte tekst til at blive længere end max-længden", combined.getCombinedTranslation().getLongText());
		
		String shortText = DosisTilTekstWrapper.convertShortText(dosage);
		Assert.assertNull(shortText);
		
		Assert.assertEquals(
			"Doseringsforløbet starter lørdag den 1. januar 2011 og gentages hver dag:\n"+
			"   Doseringsforløb:\n"+
			"   4 stk 2 gange daglig.\n   Bemærk: mod smerter og så en meeeeeeget lang supplerende tekst der får længden på den korte tekst til at blive længere end max-længden", 
			DosisTilTekstWrapper.convertLongText(dosage));
	}

	@Test
	public void testDaglig4StkModSmerterPlus4StkEfterBehovModSmerter() throws Exception {
		DosageWrapper dosage = 
			DosageWrapper.makeDosage(
				StructuresWrapper.makeStructures(
					UnitOrUnitsWrapper.makeUnit("stk"),
					StructureWrapper.makeStructure(
						1, "mod smerter", 
						DateOrDateTimeWrapper.makeDate("2011-01-01"), null,
						DayWrapper.makeDay(1,
								PlainDoseWrapper.makeDose(new BigDecimal(4)), 
								PlainDoseWrapper.makeDose(new BigDecimal(4), true)))));
		Assert.assertEquals(
			"Doseringsforløbet starter lørdag den 1. januar 2011 og gentages hver dag:\n"+
			"   Doseringsforløb:\n"+
			"   4 stk + 4 stk efter behov.\n   Bemærk: mod smerter",
			DosisTilTekstWrapper.convertLongText(dosage));
	}

	@Test
	public void testHverAndenDagEtc() throws Exception {
		DosageWrapper dosage = 
			DosageWrapper.makeDosage(
				StructuresWrapper.makeStructures(
					UnitOrUnitsWrapper.makeUnit("stk"),
					StructureWrapper.makeStructure(
						2, "mod smerter", 
						DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-14"), 
						DayWrapper.makeDay(1,
							PlainDoseWrapper.makeDose(new BigDecimal(1))), 
						DayWrapper.makeDay(2, 
							PlainDoseWrapper.makeDose(new BigDecimal(2))))));
		Assert.assertEquals(
			"Doseringsforløbet starter lørdag den 1. januar 2011, forløbet gentages hver 2. dag, og ophører fredag den 14. januar 2011.\n"+
			"Bemærk at doseringen varierer:\n"+
			"   Doseringsforløb:\n"+
			"   Dag 1: 1 stk\n"+
			"   Dag 2: 2 stk.\n   Bemærk: mod smerter",
			DosisTilTekstWrapper.convertLongText(dosage));
	}

	@Test
	public void testMorgenMiddagAftenNat() throws Exception {
		DosageWrapper dosage = 
			DosageWrapper.makeDosage(
				StructuresWrapper.makeStructures(
					UnitOrUnitsWrapper.makeUnit("stk"), 
					StructureWrapper.makeStructure(
					1, "mod smerter",
					DateOrDateTimeWrapper.makeDate("2011-01-01"),DateOrDateTimeWrapper.makeDate("2011-01-14"), 
					DayWrapper.makeDay(1,
						MorningDoseWrapper.makeDose(new BigDecimal(1)), 
						NoonDoseWrapper.makeDose(new BigDecimal(1)), 
						EveningDoseWrapper.makeDose(new BigDecimal(1)), 
						NightDoseWrapper.makeDose(new BigDecimal(1)))))); 
		Assert.assertEquals(
			"Doseringsforløbet starter lørdag den 1. januar 2011, gentages hver dag, og ophører fredag den 14. januar 2011:\n"+
			"   Doseringsforløb:\n"+
			"   1 stk morgen + 1 stk middag + 1 stk aften + 1 stk nat.\n   Bemærk: mod smerter",				
			DosisTilTekstWrapper.convertLongText(dosage));
	}
	
	@Test
	public void testMaxQuantityFullCiphers() throws Exception {
		  
		BigDecimal minimalQuantity = new BigDecimal("0.000000001");
		BigDecimal maximalQuantity = new BigDecimal("999999999.9999999");
		DosageWrapper dosage = 
			DosageWrapper.makeDosage(
				StructuresWrapper.makeStructures(
					UnitOrUnitsWrapper.makeUnit("stk"), 
					StructureWrapper.makeStructure(
					1, "mod smerter",
					DateOrDateTimeWrapper.makeDate("2011-01-01"),DateOrDateTimeWrapper.makeDate("2011-01-14"), 
					DayWrapper.makeDay(1,
						MorningDoseWrapper.makeDose(minimalQuantity, maximalQuantity)))));
		
		DailyDosis dd = DosisTilTekstWrapper.calculateDailyDosis(dosage);
		Assert.assertEquals(0, minimalQuantity.compareTo(dd.getInterval().getMinimum()));
		Assert.assertEquals(0, maximalQuantity.compareTo(dd.getInterval().getMaximum()));
	}
	
	@Test
	public void testCombinedWithFreeText() throws Exception {
		DosageWrapper dosage = 
			DosageWrapper.makeDosage(FreeTextWrapper.makeFreeText(DateOrDateTimeWrapper.makeDate("2011-01-01"), null, "Bare tag rigeligt"));
		
		DosageTranslationCombined combined = DosisTilTekstWrapper.convertCombined(dosage);
		Assert.assertEquals("Bare tag rigeligt", combined.getCombinedTranslation().getShortText());
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011.\n"+
				"   Doseringsforløb:\n"+
				"   Bare tag rigeligt", combined.getCombinedTranslation().getLongText());
	}
	
	@Test
	public void testShortTextToLong() {
		DosageWrapper dosage = DosageWrapper.makeDosage(StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"),
				StructureWrapper.makeStructure(7, "en meget, meget, meget laaaang supplerende tekst",
						DateOrDateTimeWrapper.makeDate("2012-06-08"), DateOrDateTimeWrapper.makeDate("2012-12-31"),
						DayWrapper.makeDay(1, PlainDoseWrapper.makeDose(new BigDecimal(1.0))),
						DayWrapper.makeDay(3, PlainDoseWrapper.makeDose(new BigDecimal(1.0))),
						DayWrapper.makeDay(5, PlainDoseWrapper.makeDose(new BigDecimal(1.0))),
						DayWrapper.makeDay(7, PlainDoseWrapper.makeDose(new BigDecimal(1.0))))));
		Assert.assertEquals("WeeklyRepeatedConverterImpl", DosisTilTekstWrapper.getLongTextConverterClassName(dosage));
		Assert.assertEquals(
				"Doseringsforløbet starter fredag den 8. juni 2012, forløbet gentages hver uge, og ophører mandag den 31. december 2012.\n"
						+ "Bemærk at doseringen har et komplekst forløb:\n" + "   Doseringsforløb:\n"
						+ "   Tirsdag: 1 stk\n"
						+ "   Torsdag: 1 stk\n"
						+ "   Fredag: 1 stk\n"
						+ "   Søndag: 1 stk.\n   Bemærk: en meget, meget, meget laaaang supplerende tekst",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"1 stk tirsdag, torsdag, fredag og søndag hver uge en meget, meget, meget laaaang supplerende tekst",
				DosisTilTekstWrapper.convertShortText(dosage, 300));
		Assert.assertNull(DosisTilTekstWrapper.convertShortText(dosage));
	}
}

