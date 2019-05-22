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

package dk.medicinkortet.fmkdosistiltekstwrapper.ns2009;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import dk.medicinkortet.fmkdosistiltekstwrapper.DosageType;
import dk.medicinkortet.fmkdosistiltekstwrapper.rhino.DosisTilTekstWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.DosisTilTekstWrapperTestBase;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.DateOrDateTimeWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.DayWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.DosageWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.PlainDoseWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.StructureWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.StructuresWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.UnitOrUnitsWrapper;

public class ParacetamolConverterTest extends DosisTilTekstWrapperTestBase  {
	
	@Test
	public void test1Til2stk3Til4GangeDagligVedSmerter() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					1, "ved smerter", DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-30"),
					DayWrapper.makeDay(
						1, 
						PlainDoseWrapper.makeDose(new BigDecimal(1), new BigDecimal(2)), 
						PlainDoseWrapper.makeDose(new BigDecimal(1), new BigDecimal(2)), 
						PlainDoseWrapper.makeDose(new BigDecimal(1), new BigDecimal(2)), 
						PlainDoseWrapper.makeDose(new BigDecimal(1), new BigDecimal(2), true)))));		
		Assert.assertEquals(
			"Doseringsforløbet starter lørdag den 1. januar 2011, gentages hver dag, og ophører søndag den 30. januar 2011:\n"+
			"   Doseringsforløb:\n"+
			"   1-2 stk + 1-2 stk + 1-2 stk + 1-2 stk efter behov.\n   Bemærk: ved smerter",
			DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
			"ParacetamolConverterImpl", 
			DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
			"1-2 stk 3-4 gange daglig.\n   Bemærk: ved smerter", 
			DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertTrue(DosisTilTekstWrapper.calculateDailyDosis(dosage).isNone());
		Assert.assertEquals(DosageType.Combined, DosisTilTekstWrapper.getDosageType(dosage));
	}

	@Test
	public void testFixedAndPNEquals() throws Exception {
		DosageWrapper dosage = 
			DosageWrapper.makeDosage(
				StructuresWrapper.makeStructures(
					UnitOrUnitsWrapper.makeUnit("stk"), 
					StructureWrapper.makeStructure(
						1, null, DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-11"), 
						DayWrapper.makeDay(1,
							PlainDoseWrapper.makeDose(new BigDecimal(2), true),
							PlainDoseWrapper.makeDose(new BigDecimal(2), false)))));
		Assert.assertEquals(
				"DailyRepeatedConverterImpl", 
				DosisTilTekstWrapper.getLongTextConverterClassName(dosage));
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011, gentages hver dag, og ophører tirsdag den 11. januar 2011:\n"+
				"   Doseringsforløb:\n"+
				"   2 stk efter behov + 2 stk",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"ParacetamolConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"2 stk 1-2 gange daglig", 
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertTrue(DosisTilTekstWrapper.calculateDailyDosis(dosage).isNone());
		Assert.assertEquals(DosageType.Combined, DosisTilTekstWrapper.getDosageType(dosage));				
	}

	@Test
	public void testFixedAndPNEquals2() throws Exception {
		DosageWrapper dosage = 
			DosageWrapper.makeDosage(
				StructuresWrapper.makeStructures(
					UnitOrUnitsWrapper.makeUnit("stk"), 
					StructureWrapper.makeStructure(
						1, null, DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-11"), 
						DayWrapper.makeDay(1,
							PlainDoseWrapper.makeDose(new BigDecimal(2), true), 
							PlainDoseWrapper.makeDose(new BigDecimal(2), false), 
							PlainDoseWrapper.makeDose(new BigDecimal(2), false)))));
		Assert.assertEquals(
				"DailyRepeatedConverterImpl", 
				DosisTilTekstWrapper.getLongTextConverterClassName(dosage));
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011, gentages hver dag, og ophører tirsdag den 11. januar 2011:\n"+
				"   Doseringsforløb:\n"+
				"   2 stk efter behov + 2 stk 2 gange daglig",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"ParacetamolConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"2 stk 2-3 gange daglig", 
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertTrue(DosisTilTekstWrapper.calculateDailyDosis(dosage).isNone());
		Assert.assertEquals(DosageType.Combined, DosisTilTekstWrapper.getDosageType(dosage));				
	}
	
	@Test
	public void testFixedAndPNEquals3() throws Exception {
		DosageWrapper dosage = 
			DosageWrapper.makeDosage(
				StructuresWrapper.makeStructures(
					UnitOrUnitsWrapper.makeUnit("stk"), 
					StructureWrapper.makeStructure(
						1, null, DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-11"), 
						DayWrapper.makeDay(1,
							PlainDoseWrapper.makeDose(new BigDecimal(2), true), 
							PlainDoseWrapper.makeDose(new BigDecimal(2), true), 
							PlainDoseWrapper.makeDose(new BigDecimal(2), false), 
							PlainDoseWrapper.makeDose(new BigDecimal(2), false)))));
		Assert.assertEquals(
				"DailyRepeatedConverterImpl", 
				DosisTilTekstWrapper.getLongTextConverterClassName(dosage));
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011, gentages hver dag, og ophører tirsdag den 11. januar 2011:\n"+
				"   Doseringsforløb:\n"+
	   			"   2 stk efter behov + 2 stk efter behov + 2 stk + 2 stk",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"ParacetamolConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"2 stk 2-4 gange daglig", 
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertTrue(DosisTilTekstWrapper.calculateDailyDosis(dosage).isNone());
		Assert.assertEquals(DosageType.Combined, DosisTilTekstWrapper.getDosageType(dosage));				
	}

	@Test
	public void testFixedAndPNDifferent() throws Exception {
		DosageWrapper dosage = 
			DosageWrapper.makeDosage(
				StructuresWrapper.makeStructures(
					UnitOrUnitsWrapper.makeUnit("stk"), 
					StructureWrapper.makeStructure(
						1, null, DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-11"), 
						DayWrapper.makeDay(1,
							PlainDoseWrapper.makeDose(new BigDecimal(4), true), 
							PlainDoseWrapper.makeDose(new BigDecimal(2), false)))));
		Assert.assertEquals(
				"DailyRepeatedConverterImpl", 
				DosisTilTekstWrapper.getLongTextConverterClassName(dosage));
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011, gentages hver dag, og ophører tirsdag den 11. januar 2011:\n"+
				"   Doseringsforløb:\n"+
				"   4 stk efter behov + 2 stk",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertNull(DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertNull(DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertTrue(DosisTilTekstWrapper.calculateDailyDosis(dosage).isNone());
		Assert.assertEquals(DosageType.Combined, DosisTilTekstWrapper.getDosageType(dosage));				
	}
	
	
}
