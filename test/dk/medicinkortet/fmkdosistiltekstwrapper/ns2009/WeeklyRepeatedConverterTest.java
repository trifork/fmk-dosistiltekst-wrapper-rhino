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

public class WeeklyRepeatedConverterTest extends DosisTilTekstWrapperTestBase {

	@Test 
	public void testWeeklyTwiceEveryTwoDays() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					7, "ved måltid", DateOrDateTimeWrapper.makeDate("2012-06-08"), DateOrDateTimeWrapper.makeDate("2012-12-31"), 
					DayWrapper.makeDay(
						1, 
						PlainDoseWrapper.makeDose(new BigDecimal(1)), 
						PlainDoseWrapper.makeDose(new BigDecimal(1))),
					DayWrapper.makeDay(
						3, 
						PlainDoseWrapper.makeDose(new BigDecimal(1)), 
						PlainDoseWrapper.makeDose(new BigDecimal(1))),
					DayWrapper.makeDay(
						5, 
						PlainDoseWrapper.makeDose(new BigDecimal(1)), 
						PlainDoseWrapper.makeDose(new BigDecimal(1))),
					DayWrapper.makeDay(
						7, 
						PlainDoseWrapper.makeDose(new BigDecimal(1)), 
						PlainDoseWrapper.makeDose(new BigDecimal(1))))));
		Assert.assertEquals(
				"WeeklyRepeatedConverterImpl", 
				DosisTilTekstWrapper.getLongTextConverterClassName(dosage));
		Assert.assertEquals(
				"Doseringsforløbet starter fredag den 8. juni 2012, forløbet gentages hver uge, og ophører mandag den 31. december 2012.\n"+
				"Bemærk at doseringen har et komplekst forløb:\n"+
				"   Doseringsforløb:\n"+
				"   Tirsdag: 1 stk 2 gange\n"+
				"   Torsdag: 1 stk 2 gange\n"+
				"   Fredag: 1 stk 2 gange\n"+
				"   Søndag: 1 stk 2 gange.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertLongText(dosage));
//		Assert.assertEquals(
//				dk.medicinkortet.fmkdosistiltekstwrapper.shorttextconverterimpl."WeeklyRepeatedConverterImpl", 
//				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
//		Assert.assertEquals("1 stk 2 gange daglig tirsdag, torsdag, fredag og søndag hver uge ved måltid", DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				8/7., 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001); 							
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));						
	}
	
	@Test 
	public void testWeeklyOnceEveryTwoDays() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					7, "ved måltid", DateOrDateTimeWrapper.makeDate("2012-06-08"), DateOrDateTimeWrapper.makeDate("2012-12-31"), 
					DayWrapper.makeDay(
						1, 
						PlainDoseWrapper.makeDose(new BigDecimal(1))),
					DayWrapper.makeDay(
						3, 
						PlainDoseWrapper.makeDose(new BigDecimal(1))),
					DayWrapper.makeDay(
						5, 
						PlainDoseWrapper.makeDose(new BigDecimal(1))),
					DayWrapper.makeDay(
						7, 
						PlainDoseWrapper.makeDose(new BigDecimal(1))))));
		Assert.assertEquals(
				"WeeklyRepeatedConverterImpl", 
				DosisTilTekstWrapper.getLongTextConverterClassName(dosage));
		Assert.assertEquals(
				"Doseringsforløbet starter fredag den 8. juni 2012, forløbet gentages hver uge, og ophører mandag den 31. december 2012.\n"+
				"Bemærk at doseringen har et komplekst forløb:\n"+
				"   Doseringsforløb:\n"+
				"   Tirsdag: 1 stk\n"+
				"   Torsdag: 1 stk\n"+
				"   Fredag: 1 stk\n"+
				"   Søndag: 1 stk.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"WeeklyRepeatedConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals("1 stk tirsdag, torsdag, fredag og søndag hver uge.\n   Bemærk: ved måltid", DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				4/7., 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001); 							
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));						
	}
	
}
