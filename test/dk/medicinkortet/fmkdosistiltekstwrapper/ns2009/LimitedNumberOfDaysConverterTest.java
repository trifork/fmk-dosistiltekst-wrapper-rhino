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
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.LocalTime;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.PlainDoseWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.StructureWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.StructuresWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.TimedDoseWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.UnitOrUnitsWrapper;

public class LimitedNumberOfDaysConverterTest extends DosisTilTekstWrapperTestBase {
	
	@Test
	public void test4Stk2GangeDagligI3DageVedMaaltid() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					0, "ved måltid", 
					DateOrDateTimeWrapper.makeDate("2011-01-01"), null, 
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
				"DefaultLongTextConverterImpl",
				DosisTilTekstWrapper.getLongTextConverterClassName(dosage));
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011 og ophører efter det angivne forløb:\n"+
				"   Doseringsforløb:\n"+
				"   Lørdag den 1. januar 2011: 4 stk 2 gange\n"+
				"   Søndag den 2. januar 2011: 4 stk 2 gange\n"+
				"   Mandag den 3. januar 2011: 4 stk 2 gange\n"+
				"   Tirsdag den 4. januar 2011: 4 stk 2 gange.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"LimitedNumberOfDaysConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"4 stk 2 gange daglig i 4 dage.\n   Bemærk: ved måltid", 
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				8.0, 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001); 				
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));
	}

	@Test
	public void test4Til6Stk2GangeDagligI3DageVedMaaltid() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					0, "ved måltid", DateOrDateTimeWrapper.makeDate("2011-01-01"), null,  
					DayWrapper.makeDay(
						1, 
						PlainDoseWrapper.makeDose(new BigDecimal(4), new BigDecimal(6)), 
						PlainDoseWrapper.makeDose(new BigDecimal(4), new BigDecimal(6))), 
					DayWrapper.makeDay(
						2, 
						PlainDoseWrapper.makeDose(new BigDecimal(4), new BigDecimal(6)), 
						PlainDoseWrapper.makeDose(new BigDecimal(4), new BigDecimal(6))), 
					DayWrapper.makeDay(
						3, 
						PlainDoseWrapper.makeDose(new BigDecimal(4), new BigDecimal(6)), 
						PlainDoseWrapper.makeDose(new BigDecimal(4), new BigDecimal(6))))));		
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011 og ophører efter det angivne forløb:\n"+
				"   Doseringsforløb:\n"+
				"   Lørdag den 1. januar 2011: 4-6 stk 2 gange\n"+
				"   Søndag den 2. januar 2011: 4-6 stk 2 gange\n"+
				"   Mandag den 3. januar 2011: 4-6 stk 2 gange.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"LimitedNumberOfDaysConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));		
		Assert.assertEquals(
				"4-6 stk 2 gange daglig i 3 dage.\n   Bemærk: ved måltid", 
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				8.0, 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getInterval().getMinimum().doubleValue(), 
				0.000000001); 				
		Assert.assertEquals(
				12.0, 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getInterval().getMaximum().doubleValue(), 
				0.000000001); 				
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));
	}

	@Test
	public void testCQ611() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("ml"), 
				StructureWrapper.makeStructure(
					0, "ved måltid", DateOrDateTimeWrapper.makeDate("2011-01-01"), null, 
					DayWrapper.makeDay(
						3, 
						TimedDoseWrapper.makeDose(new LocalTime(11,25), new BigDecimal(7), false)))));
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011 og ophører efter det angivne forløb:\n" +
				"   Doseringsforløb:\n" +
				"   Mandag den 3. januar 2011: 7 ml kl. 11:25.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertNull(DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				7./3., 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001); 							
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));
	}
	
	// FMK-3017 Dosis-to-text oversætter NotIterated struktur med en enkelt dag forkert
	@Test
	public void test5ml4gange() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("ml"), 
				StructureWrapper.makeStructure(
					0, null, 
					DateOrDateTimeWrapper.makeDate("2010-01-01"), DateOrDateTimeWrapper.makeDate("2110-01-01"), 
					DayWrapper.makeDay(
						1, 
						PlainDoseWrapper.makeDose(new BigDecimal(5), true), 
						PlainDoseWrapper.makeDose(new BigDecimal(5), true), 
						PlainDoseWrapper.makeDose(new BigDecimal(5), true),
						PlainDoseWrapper.makeDose(new BigDecimal(5), true)))));			
					
		Assert.assertEquals(
				"5 ml efter behov 4 gange", 
				DosisTilTekstWrapper.convertShortText(dosage));
	}
	
	
}
