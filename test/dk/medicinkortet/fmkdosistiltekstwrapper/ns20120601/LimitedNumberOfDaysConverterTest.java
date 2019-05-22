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

/**
 * The purpose of this test class is to test new functionality added in FMK 1.4 (2012/06/01 namespace). 
 * The test of the general functionality is done in the testclass of the same name in the 
 * dk.medicinkortet.fmkdosistiltekstwrapper.ns2009 package. 
 */
public class LimitedNumberOfDaysConverterTest extends DosisTilTekstWrapperTestBase {
	
	@Test
	public void testUnits() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnits("måleskefuld", "måleskefulde"), 
				StructureWrapper.makeStructure(
					0, "ved måltid", DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-04"), 
					DayWrapper.makeDay(
						1, 
						PlainDoseWrapper.makeDose(new BigDecimal(4)), 
						PlainDoseWrapper.makeDose(new BigDecimal(4))), 
					DayWrapper.makeDay(
						2, 
						PlainDoseWrapper.makeDose(new BigDecimal(4)), 
						PlainDoseWrapper.makeDose(new BigDecimal(4))), 
					DayWrapper.makeDay(
						3, 
						PlainDoseWrapper.makeDose(new BigDecimal(4)), 
						PlainDoseWrapper.makeDose(new BigDecimal(4))), 
					DayWrapper.makeDay(
						4, 
						PlainDoseWrapper.makeDose(new BigDecimal(4)), 
						PlainDoseWrapper.makeDose(new BigDecimal(4))))));		
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011, og ophører tirsdag den 4. januar 2011:\n"+
				"   Doseringsforløb:\n"+
				"   Lørdag den 1. januar 2011: 4 måleskefulde 2 gange\n"+
				"   Søndag den 2. januar 2011: 4 måleskefulde 2 gange\n"+
				"   Mandag den 3. januar 2011: 4 måleskefulde 2 gange\n"+
				"   Tirsdag den 4. januar 2011: 4 måleskefulde 2 gange.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"LimitedNumberOfDaysConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"4 måleskefulde 2 gange daglig i 4 dage.\n   Bemærk: ved måltid", 
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				8.0, 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001); 				
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));
	}
	
	@Test
	public void testAccordingToNeed() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnits("måleskefuld", "måleskefulde"),  
				StructureWrapper.makeStructure(
					0, "ved måltid", DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-04"),  
					DayWrapper.makeDay(
						1, 
						PlainDoseWrapper.makeDose(new BigDecimal(4), true), 
						PlainDoseWrapper.makeDose(new BigDecimal(4), true)), 
					DayWrapper.makeDay(
						2, 
						PlainDoseWrapper.makeDose(new BigDecimal(4), true), 
						PlainDoseWrapper.makeDose(new BigDecimal(4), true)), 
					DayWrapper.makeDay(
						3, 
						PlainDoseWrapper.makeDose(new BigDecimal(4), true), 
						PlainDoseWrapper.makeDose(new BigDecimal(4), true)), 
					DayWrapper.makeDay(
						4, 
						PlainDoseWrapper.makeDose(new BigDecimal(4), true), 
						PlainDoseWrapper.makeDose(new BigDecimal(4), true)))));		
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011, og ophører tirsdag den 4. januar 2011:\n"+
				"   Doseringsforløb:\n"+
				"   Lørdag den 1. januar 2011: 4 måleskefulde efter behov højst 2 gange\n"+
				"   Søndag den 2. januar 2011: 4 måleskefulde efter behov højst 2 gange\n"+
				"   Mandag den 3. januar 2011: 4 måleskefulde efter behov højst 2 gange\n"+
				"   Tirsdag den 4. januar 2011: 4 måleskefulde efter behov højst 2 gange.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"LimitedNumberOfDaysConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"4 måleskefulde efter behov 2 gange daglig i 4 dage.\n   Bemærk: ved måltid", 
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertNull(DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue()); 				
		Assert.assertEquals(DosageType.AccordingToNeed, DosisTilTekstWrapper.getDosageType(dosage));
	}

	@Test
	public void testJustOne() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnits("tablet", "tabletter"),  
				StructureWrapper.makeStructure(
					0, "ved måltid", DateOrDateTimeWrapper.makeDate("2011-01-01"), null,  
					DayWrapper.makeDay(
						1, 
						PlainDoseWrapper.makeDose(new BigDecimal(4))))));		
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011 og ophører efter det angivne forløb:\n"+
				"   Doseringsforløb:\n"+
				"   Lørdag den 1. januar 2011: 4 tabletter.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"LimitedNumberOfDaysConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"4 tabletter 1 gang.\n   Bemærk: ved måltid", 
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(4.0, DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 0.000000001); 				
		Assert.assertEquals(DosageType.OneTime, DosisTilTekstWrapper.getDosageType(dosage));
	}

}
