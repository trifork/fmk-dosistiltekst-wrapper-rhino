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

public class SimpleNonRepeatedConverterTest extends DosisTilTekstWrapperTestBase {
	
	@Test
	public void test1Plaster5TimerFoerVirkningOenskes() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("plaster"),
				StructureWrapper.makeStructure(
					0,  "5 timer før virkning ønskes", DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-30"), 
					DayWrapper.makeDay(
						0, 
						PlainDoseWrapper.makeDose(new BigDecimal(1))))));				
		Assert.assertEquals(
			"Doseringsforløbet starter lørdag den 1. januar 2011, og ophører søndag den 30. januar 2011:\n"+
			"   Doseringsforløb:\n"+
			"   Dag ikke angivet: 1 plaster.\n   Bemærk: 5 timer før virkning ønskes",
			DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
			"SimpleNonRepeatedConverterImpl", 
			DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
			"1 plaster 5 timer før virkning ønskes", 
			DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertTrue(DosisTilTekstWrapper.calculateDailyDosis(dosage).isNone());
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));						
	}
	
	@Test
	public void testOneDayOnly() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("kapsel"),
				StructureWrapper.makeStructure(
					0,  "dagen før indlæggelse", DateOrDateTimeWrapper.makeDate("2011-01-01"), null, 
					DayWrapper.makeDay(
						1, 
						PlainDoseWrapper.makeDose(new BigDecimal(1)), 
						PlainDoseWrapper.makeDose(new BigDecimal(1))))));				
		Assert.assertEquals(
			"Doseringsforløbet starter lørdag den 1. januar 2011 og ophører efter det angivne forløb:\n"+
			"   Doseringsforløb:\n"+
			"   Lørdag den 1. januar 2011: 1 kapsel 2 gange.\n   Bemærk: dagen før indlæggelse",
			DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"LimitedNumberOfDaysConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
			"1 kapsel 2 gange dagen før indlæggelse", 
			DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(2, DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 0.000000001);
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));						
	}
	
	@Test
	public void test1StkKl0730FoerIndlaeggelse() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"),
				StructureWrapper.makeStructure(
					0, "før indlæggelse", DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-01"), 
					DayWrapper.makeDay(
						0, 
						TimedDoseWrapper.makeDose(new LocalTime(7,30), new BigDecimal(1), false)))));
		Assert.assertEquals(
			"Doseringen foretages kun lørdag den 1. januar 2011:\n"+
			"   Doseringsforløb:\n"+
			"   Dag ikke angivet: 1 stk kl. 07:30.\n   Bemærk: før indlæggelse",
			DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"SimpleNonRepeatedConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
			"1 stk kl. 07:30 før indlæggelse", 
			DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertTrue(DosisTilTekstWrapper.calculateDailyDosis(dosage).isNone());
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));						
	}	

	@Test
	public void test1StkKl0730() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					0, null, DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-01"), 
					DayWrapper.makeDay(
						1, 
						TimedDoseWrapper.makeDose(new LocalTime(7,30), new BigDecimal(1), false)))));
		Assert.assertEquals(
			"Doseringen foretages kun lørdag den 1. januar 2011:\n"+
			"   Doseringsforløb:\n"+
			"   Lørdag den 1. januar 2011: 1 stk kl. 07:30",
			DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"SimpleNonRepeatedConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
			"1 stk kl. 07:30", 
			DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				1.0, 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001);
		Assert.assertEquals(DosageType.OneTime, DosisTilTekstWrapper.getDosageType(dosage));						
	}	
		
}
