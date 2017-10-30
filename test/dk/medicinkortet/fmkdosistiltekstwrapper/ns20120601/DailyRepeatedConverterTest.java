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

package dk.medicinkortet.fmkdosistiltekstwrapper.ns20120601;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import dk.medicinkortet.fmkdosistiltekstwrapper.DosageType;
import dk.medicinkortet.fmkdosistiltekstwrapper.rhino.DosisTilTekstWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.DosisTilTekstWrapperTestBase;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.DateOrDateTimeWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.DayWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.DosageWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.LocalTime;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.PlainDoseWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.StructureWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.StructuresWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.TimedDoseWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.UnitOrUnitsWrapper;

/**
 * The purpose of this test class is to test new functionality added in FMK 1.4 (2012/06/01 namespace). 
 * The test of the general functionality is done in the testclass of the same name in the 
 * dk.medicinkortet.fmkdosistiltekstwrapper.ns2009 package. 
 */
public class DailyRepeatedConverterTest extends DosisTilTekstWrapperTestBase {

	@Test
	public void testUnits() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnits("tablet", "tabletter"), 
				StructureWrapper.makeStructure(
					1, 
					"ved måltid", 
					DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-30"), 
					DayWrapper.makeDay(
						1, 
						PlainDoseWrapper.makeDose(new BigDecimal(1)), 
						PlainDoseWrapper.makeDose(new BigDecimal(1)), 
						PlainDoseWrapper.makeDose(new BigDecimal(1), true)))));
		Assert.assertEquals(
				"DailyRepeatedConverterImpl", 
				DosisTilTekstWrapper.getLongTextConverterClassName(dosage));
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011, gentages hver dag, og ophører søndag den 30. januar 2011:\n"+
				"   Doseringsforløb:\n"+
				"   1 tablet ved måltid + 1 tablet ved måltid + 1 tablet efter behov ved måltid",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"ParacetamolConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"1 tablet 2-3 gange daglig ved måltid", 
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertNull(DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue());
		Assert.assertEquals(DosageType.Combined, DosisTilTekstWrapper.getDosageType(dosage));
	}
	
	@Test
	public void testAccordingToNeed() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnits("tablet", "tabletter"), 
				StructureWrapper.makeStructure(
					1, 
					"ved måltid", 
					DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-30"), 
					DayWrapper.makeDay(
						1, 
						PlainDoseWrapper.makeDose(new BigDecimal(1), false), 
						PlainDoseWrapper.makeDose(new BigDecimal(1), false), 
						PlainDoseWrapper.makeDose(new BigDecimal(1), true)))));
		Assert.assertEquals(
				"DailyRepeatedConverterImpl", 
				DosisTilTekstWrapper.getLongTextConverterClassName(dosage));
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011, gentages hver dag, og ophører søndag den 30. januar 2011:\n"+
				"   Doseringsforløb:\n"+
				"   1 tablet ved måltid + 1 tablet ved måltid + 1 tablet efter behov ved måltid",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"ParacetamolConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"1 tablet 2-3 gange daglig ved måltid", 
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertNull(DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue());
		Assert.assertEquals(DosageType.Combined, DosisTilTekstWrapper.getDosageType(dosage));
	}
	
	/* FMK-2444 Combined TimeDose/PlainDose was mistakenly converted by ParacetamolConverter */
	@Test
	public void testCombinedTimePlain() {
		
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnits("tablet", "tabletter"), 
				StructureWrapper.makeStructure(
					1, 
					null, 
					DateOrDateTimeWrapper.makeDate("2011-01-01"), null, //DateOrDateTimeWrapper.makeDate("2011-01-01"), 
					DayWrapper.makeDay(
						1, 
						TimedDoseWrapper.makeDose(new LocalTime(8,00), new BigDecimal(1), false), 
						TimedDoseWrapper.makeDose(new LocalTime(12,00), new BigDecimal(1), false), 
						TimedDoseWrapper.makeDose(new LocalTime(16,00), new BigDecimal(1), false), 
						TimedDoseWrapper.makeDose(new LocalTime(20,00), new BigDecimal(1), false), 
						PlainDoseWrapper.makeDose(new BigDecimal(1), true),
						PlainDoseWrapper.makeDose(new BigDecimal(1), true),
						PlainDoseWrapper.makeDose(new BigDecimal(1), true)
					)
				)
			)
		);

		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011 og gentages hver dag:\n" + 
				"   Doseringsforløb:\n" + 
				"   1 tablet kl. 08:00 + 1 tablet kl. 12:00 + 1 tablet kl. 16:00 + 1 tablet kl. 20:00 + 1 tablet efter behov + 1 tablet efter behov + 1 tablet efter behov",
				DosisTilTekstWrapper.convertLongText(dosage));

		Assert.assertEquals(
			"DailyRepeatedConverterImpl", 
			DosisTilTekstWrapper.getLongTextConverterClassName(dosage));
		String shorttext= DosisTilTekstWrapper.convertShortText(dosage);
		Assert.assertNull(DosisTilTekstWrapper.convertShortText(dosage, 1000));	/* no known converter */
		Assert.assertNull(DosisTilTekstWrapper.getShortTextConverterClassName(dosage)); /* no known converter */
		Assert.assertNull(DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue());
		Assert.assertEquals(DosageType.Combined, DosisTilTekstWrapper.getDosageType(dosage));
	}
	
	// FMK-2606 DosageLong tekst viser ikke "1 gang daglig" i longtext
	@Test
	public void testDailyRepeatedWithOneDailyDosage() throws Exception {
		
		DosageWrapper dosage = DosageWrapper.makeDosage(
				StructuresWrapper.makeStructures(
					UnitOrUnitsWrapper.makeUnits("ml", "ml"), 
					StructureWrapper.makeStructure(
						1, null, DateOrDateTimeWrapper.makeDate("2013-01-01"), DateOrDateTimeWrapper.makeDate("2013-08-15"),
						DayWrapper.makeDay(
							1, 
							PlainDoseWrapper.makeDose(new BigDecimal(12)))
						)
				));
		
		String shortText = DosisTilTekstWrapper.convertShortText(dosage);
		String longText = DosisTilTekstWrapper.convertLongText(dosage);
		assertTrue(longText.contains("12 ml 1 gang daglig"));
	}

}
