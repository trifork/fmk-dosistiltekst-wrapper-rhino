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
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.LocalTime;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.MorningDoseWrapper;
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
public class RepeatedConverterTest extends DosisTilTekstWrapperTestBase {
	
	@Test
	public void testUnits() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnits("dåse", "dåser"), 
				StructureWrapper.makeStructure(
					1, null, DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-30"),  
					DayWrapper.makeDay(
						1, 
						PlainDoseWrapper.makeDose(new BigDecimal(3)), 
						PlainDoseWrapper.makeDose(new BigDecimal(3))))));
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011, gentages hver dag, og ophører søndag den 30. januar 2011:\n"+
				"   Doseringsforløb:\n"+
				"   3 dåser 2 gange daglig",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"RepeatedConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"3 dåser 2 gange daglig", 
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				6.0, 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001);
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));		
	}
	
	@Test
	public void testAccordingToNeed() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnits("dåse", "dåser"), 
				StructureWrapper.makeStructure(
					1, null, DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-30"),  
					DayWrapper.makeDay(
						1, 
						PlainDoseWrapper.makeDose(new BigDecimal(3), true), 
						PlainDoseWrapper.makeDose(new BigDecimal(3), true)))));
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011, gentages hver dag, og ophører søndag den 30. januar 2011:\n"+
				"   Doseringsforløb:\n"+
				"   3 dåser efter behov højst 2 gange daglig",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"SimpleLimitedAccordingToNeedConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"3 dåser efter behov, højst 2 gange daglig", 
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertNull(DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue());
		Assert.assertEquals(DosageType.AccordingToNeed, DosisTilTekstWrapper.getDosageType(dosage));		
	}
	
	@Test
	public void testOnceWeekly() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					7, "ved måltid", DateOrDateTimeWrapper.makeDate("2013-08-12"), null, 
					DayWrapper.makeDay(
						3, 
						TimedDoseWrapper.makeDose(new LocalTime(8, 0), new BigDecimal(1), false)))));
		Assert.assertEquals(
				"WeeklyRepeatedConverterImpl", 
				DosisTilTekstWrapper.getLongTextConverterClassName(dosage));
		Assert.assertEquals(
				"Doseringsforløbet starter mandag den 12. august 2013, forløbet gentages hver uge:\n"+
				"   Doseringsforløb:\n"+
				"   Onsdag: 1 stk kl. 08:00.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"RepeatedConverterImpl",
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"1 stk kl. 08:00 onsdag hver uge.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				1/7., 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001); 							
		Assert.assertEquals(DosageType.Fixed, DosisTilTekstWrapper.getDosageType(dosage));						
	}
	
	@Test
	public void testOnceWeekly2() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					7, "ved måltid", DateOrDateTimeWrapper.makeDate("2013-08-12"), null, 
					DayWrapper.makeDay(
						3, 
						MorningDoseWrapper.makeDose(new BigDecimal(1))))));
		Assert.assertEquals(
				"WeeklyRepeatedConverterImpl", 
				DosisTilTekstWrapper.getLongTextConverterClassName(dosage));
		Assert.assertEquals(
				"Doseringsforløbet starter mandag den 12. august 2013, forløbet gentages hver uge:\n"+
				"   Doseringsforløb:\n"+
				"   Onsdag: 1 stk morgen.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"WeeklyMorningNoonEveningNightConverterImpl",
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"1 stk morgen onsdag hver uge.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				1/7., 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001); 							
		Assert.assertEquals(DosageType.Fixed, DosisTilTekstWrapper.getDosageType(dosage));						
	}
	

	// FMK-2958 ASCP00078113 Dosis-til-text: slutdato mangler i ugentligt gentagede doseringer
	@Test
	public void testOnceWeeklyWithEndDate() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					7, "ved måltid", DateOrDateTimeWrapper.makeDate("2013-08-12"), DateOrDateTimeWrapper.makeDate("2013-09-12"), 
					DayWrapper.makeDay(
						3, 
						MorningDoseWrapper.makeDose(new BigDecimal(1))))));
		Assert.assertEquals(
				"WeeklyRepeatedConverterImpl", 
				DosisTilTekstWrapper.getLongTextConverterClassName(dosage));
		Assert.assertEquals(
				"Doseringsforløbet starter mandag den 12. august 2013, forløbet gentages hver uge, og ophører torsdag den 12. september 2013:\n"+
				"   Doseringsforløb:\n"+
				"   Onsdag: 1 stk morgen.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"WeeklyMorningNoonEveningNightConverterImpl",
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"1 stk morgen onsdag hver uge.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				1/7., 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001); 							
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));						
	}
	
	
	@Test
	public void testOnceEvery7thWeek() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					7*7, "ved måltid", DateOrDateTimeWrapper.makeDate("2013-08-12"), null, 
					DayWrapper.makeDay(
						3, 
						PlainDoseWrapper.makeDose(new BigDecimal(1))))));
		Assert.assertEquals(
				"DefaultLongTextConverterImpl", 
				DosisTilTekstWrapper.getLongTextConverterClassName(dosage));
		Assert.assertEquals(
				"Doseringsforløbet starter mandag den 12. august 2013, forløbet gentages efter 49 dage:\n"+
				"   Doseringsforløb:\n"+
				"   Onsdag den 14. august 2013: 1 stk.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"RepeatedConverterImpl",
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"1 stk onsdag hver 7. uge.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				1/(7.*7.), 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001); 							
		Assert.assertEquals(DosageType.Fixed, DosisTilTekstWrapper.getDosageType(dosage));						
	}
	
	@Test
	public void testOnceEvery2thMonth() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					60, "ved måltid", DateOrDateTimeWrapper.makeDate("2013-08-12"), null, 
					DayWrapper.makeDay(
						3, 
						PlainDoseWrapper.makeDose(new BigDecimal(1))))));
		Assert.assertEquals(
				"DefaultLongTextConverterImpl", 
				DosisTilTekstWrapper.getLongTextConverterClassName(dosage));
		Assert.assertEquals(
				"Doseringsforløbet starter mandag den 12. august 2013, forløbet gentages efter 60 dage:\n"+
				"   Doseringsforløb:\n"+
				"   Onsdag den 14. august 2013: 1 stk.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"RepeatedConverterImpl",
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"1 stk hver 2. måned.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				1/60., 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001); 							
		Assert.assertEquals(DosageType.Fixed, DosisTilTekstWrapper.getDosageType(dosage));						
	}
		
	@Test
	public void testMany() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnits("tablet", "tabletter"),  
				StructureWrapper.makeStructure(
					1, "ved måltid", DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-04"),  
					DayWrapper.makeDay(
						1, 
						PlainDoseWrapper.makeDose(new BigDecimal(4))))));		
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011, gentages hver dag, og ophører tirsdag den 4. januar 2011:\n"+
				"   Doseringsforløb:\n"+
				"   4 tabletter 1 gang daglig.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"RepeatedConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"4 tabletter 1 gang daglig.\n   Bemærk: ved måltid", 
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(4.0, DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 0.000000001); 				
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));
	}
	
	@Test
	public void testNew() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnits("plaster", "plastre"),  
				StructureWrapper.makeStructure(
					35, null, DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-04"),  
					DayWrapper.makeDay(
						1, 
						PlainDoseWrapper.makeDose(new BigDecimal(1))), 
					DayWrapper.makeDay(
						8,
						PlainDoseWrapper.makeDose(new BigDecimal(1))), 
					DayWrapper.makeDay(
						15, 
						PlainDoseWrapper.makeDose(new BigDecimal(1))))));		
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011, forløbet gentages efter 35 dage, og ophører tirsdag den 4. januar 2011.\n"+
				"Bemærk at doseringen har et komplekst forløb:\n"+
				"   Doseringsforløb:\n"+
				"   Lørdag den 1. januar 2011: 1 plaster\n"+
				"   Lørdag den 8. januar 2011: 1 plaster\n"+
				"   Lørdag den 15. januar 2011: 1 plaster",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"DayInWeekConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"1 plaster daglig lørdag i de første 3 uger, herefter 2 ugers pause", 
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(3/35.0, DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 0.000000001); 				
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));
	}
	
}
