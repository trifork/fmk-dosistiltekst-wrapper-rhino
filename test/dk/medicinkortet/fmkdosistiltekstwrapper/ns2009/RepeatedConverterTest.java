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

import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.*;
import org.junit.Assert;
import org.junit.Test;

import dk.medicinkortet.fmkdosistiltekstwrapper.DosageType;
import dk.medicinkortet.fmkdosistiltekstwrapper.rhino.DosisTilTekstWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.DosisTilTekstWrapperTestBase;

public class RepeatedConverterTest extends DosisTilTekstWrapperTestBase {
	
	@Test
	public void test3stk2gangeDaglig() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					1, null, DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-30"), 
					DayWrapper.makeDay(
						1, 
						PlainDoseWrapper.makeDose(new BigDecimal(3)), 
						PlainDoseWrapper.makeDose(new BigDecimal(3))))));
		Assert.assertEquals(
				"DailyRepeatedConverterImpl", 
				DosisTilTekstWrapper.getLongTextConverterClassName(dosage));
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011, gentages hver dag, og ophører søndag den 30. januar 2011:\n"+
				"   Doseringsforløb:\n"+
				"   3 stk 2 gange daglig",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"RepeatedConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"3 stk 2 gange daglig", 
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				6.0, 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001);
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));		
	}
	
	@Test
	public void test1stk3gangeDagligVedMaaltid() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					1, "ved måltid", DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-30"), 
					DayWrapper.makeDay(
						1, 
						PlainDoseWrapper.makeDose(new BigDecimal(3)), 
						PlainDoseWrapper.makeDose(new BigDecimal(3))))));
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011, gentages hver dag, og ophører søndag den 30. januar 2011:\n"+
				"   Doseringsforløb:\n"+
				"   3 stk 2 gange daglig.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"RepeatedConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"3 stk 2 gange daglig.\n   Bemærk: ved måltid", 
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				6.0, 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001); 			
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));		
	}
	
	@Test
	public void test1stkDagligVedMaaltid() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					1, "ved måltid", DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-30"), 
					DayWrapper.makeDay(
						1, 
						PlainDoseWrapper.makeDose(new BigDecimal(1))))));
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011, gentages hver dag, og ophører søndag den 30. januar 2011:\n"+
				"   Doseringsforløb:\n"+
				"   1 stk 1 gang daglig.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"RepeatedConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"1 stk daglig.\n   Bemærk: ved måltid", 
				DosisTilTekstWrapper.convertShortText(dosage));
		// assertEquals(10.0, result.getAvgDailyDosis().doubleValue()); 				
	}

	@Test
	public void test1Til2stk2GangeDaglig() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					1, null, DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-30"), 
					DayWrapper.makeDay(
						1, 
						PlainDoseWrapper.makeDose(new BigDecimal(1), new BigDecimal(2)), 
						PlainDoseWrapper.makeDose(new BigDecimal(1), new BigDecimal(2))))));
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011, gentages hver dag, og ophører søndag den 30. januar 2011:\n"+
				"   Doseringsforløb:\n"+
				"   1-2 stk 2 gange daglig",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"RepeatedConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"1-2 stk 2 gange daglig", 
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				2.0, 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getInterval().getMinimum().doubleValue(), 
				0.000000001);
		Assert.assertEquals(
				4.0, 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getInterval().getMaximum().doubleValue(), 
				0.000000001); 			
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));		
	}

	@Test
	public void test1stkHver2DagVedMaaltid() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					2, "ved måltid", DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-30"), 
					DayWrapper.makeDay(
						1, 
						PlainDoseWrapper.makeDose(new BigDecimal(1))))));
		Assert.assertEquals(
				"TwoDaysRepeatedConverterImpl", 
				DosisTilTekstWrapper.getLongTextConverterClassName(dosage));
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011, forløbet gentages hver 2. dag, og ophører søndag den 30. januar 2011:\n"+
				"   Doseringsforløb:\n"+
				"   Dag 1: 1 stk.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"RepeatedConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"1 stk hver 2. dag.\n   Bemærk: ved måltid", 
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				0.5, 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001); 		
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));		
	}
	
	
	@Test
	public void test1stkOmUgenVedMaaltid() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					7, "ved måltid", DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-30"), 
					DayWrapper.makeDay(
						1, 
						PlainDoseWrapper.makeDose(new BigDecimal(1))))));
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011, forløbet gentages hver uge, og ophører søndag den 30. januar 2011:\n"+
				"   Doseringsforløb:\n"+
				"   Lørdag: 1 stk.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"RepeatedConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"1 stk lørdag hver uge.\n   Bemærk: ved måltid", 
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				1/7., 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001); 				
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));		
	}

	@Test
	public void test1stkOmMaanedenVedMaaltid() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					30, "ved måltid", DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2013-01-01"), 
					DayWrapper.makeDay(
						1, 
						PlainDoseWrapper.makeDose(new BigDecimal(1))))));
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011, forløbet gentages efter 30 dage, og ophører tirsdag den 1. januar 2013:\n"+
				"   Doseringsforløb:\n"+
				"   Lørdag den 1. januar 2011: 1 stk.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"RepeatedConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"1 stk 1 gang om måneden.\n   Bemærk: ved måltid", 
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				1/30., 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001); 				
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));		
	}
	
	@Test
	public void test2_5stk1GangOmUgenVedMaaltid() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					7, "ved måltid", DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2013-01-01"), 
					DayWrapper.makeDay(
						1, 
						PlainDoseWrapper.makeDose(new BigDecimal(2.5))))));
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011, forløbet gentages hver uge, og ophører tirsdag den 1. januar 2013:\n"+
				"   Doseringsforløb:\n"+
				"   Lørdag: 2,5 stk.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"RepeatedConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"2,5 stk lørdag hver uge.\n   Bemærk: ved måltid", 
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				2.5/7., 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001); 				
	}
	
	@Test
	public void test2_5Stk2GangeSammeDag1GangOmUgenVedMaaltid() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					7, "ved måltid", DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2013-01-01"), 
					DayWrapper.makeDay(
						1, 
						PlainDoseWrapper.makeDose(new BigDecimal(2.5)), 
						PlainDoseWrapper.makeDose(new BigDecimal(2.5))))));
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011, forløbet gentages hver uge, og ophører tirsdag den 1. januar 2013:\n"+
				"   Doseringsforløb:\n"+
				"   Lørdag: 2,5 stk 2 gange.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"RepeatedConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"2,5 stk 2 gange lørdag hver uge.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				5/7., 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001);
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));		
	}

	@Test
	public void test2_5stk1GangOmMaanedenVedMaaltid() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					30, "ved måltid", DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2013-01-01"), 
					DayWrapper.makeDay(
						1,
						PlainDoseWrapper.makeDose(new BigDecimal(2.5))))));
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011, forløbet gentages efter 30 dage, og ophører tirsdag den 1. januar 2013:\n"+
				"   Doseringsforløb:\n"+
				"   Lørdag den 1. januar 2011: 2,5 stk.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"RepeatedConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"2,5 stk 1 gang om måneden.\n   Bemærk: ved måltid", 
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				2.5/30., 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001);
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));		
	}
	
	@Test
	public void test2_5Stk2GangeSammeDag1GangOmMaanedenVedMaaltid() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					30, "ved måltid", DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2013-01-01"),  
					DayWrapper.makeDay(
						1, 
						PlainDoseWrapper.makeDose(new BigDecimal(2.5)), 
						PlainDoseWrapper.makeDose(new BigDecimal(2.5))))));
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011, forløbet gentages efter 30 dage, og ophører tirsdag den 1. januar 2013:\n"+
				"   Doseringsforløb:\n"+
				"   Lørdag den 1. januar 2011: 2,5 stk 2 gange.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"RepeatedConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"2,5 stk 2 gange samme dag 1 gang om måneden.\n   Bemærk: ved måltid", 
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				5/30., 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001); 				
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));				
	}

	@Test
	public void test2_5stkHver5DagVedMaaltid() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					5, "ved måltid", DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2013-01-01"), 
					DayWrapper.makeDay(
						1, 
						PlainDoseWrapper.makeDose(new BigDecimal(2.5))))));
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011, forløbet gentages efter 5 dage, og ophører tirsdag den 1. januar 2013:\n"+
				"   Doseringsforløb:\n"+
				"   Lørdag den 1. januar 2011: 2,5 stk.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"RepeatedConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"2,5 stk hver 5. dag.\n   Bemærk: ved måltid", 
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				2.5/5., 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001); 			
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));				
	}
	
	@Test
	public void test2_5stk2GangeSammeDagHver5DagVedMaaltid() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					5, "ved måltid", DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2013-01-01"), 
					DayWrapper.makeDay(
						1, 
						PlainDoseWrapper.makeDose(new BigDecimal(2.5)), 
						PlainDoseWrapper.makeDose(new BigDecimal(2.5))))));
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011, forløbet gentages efter 5 dage, og ophører tirsdag den 1. januar 2013:\n"+
				"   Doseringsforløb:\n"+
				"   Lørdag den 1. januar 2011: 2,5 stk 2 gange.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"RepeatedConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"2,5 stk 2 gange samme dag hver 5. dag.\n   Bemærk: ved måltid", 
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				1, 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001); 		
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));				
	}
	
	@Test
	public void test0_5stk1GangSammeDagHver5DagVedMaaltid() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					5, "ved måltid", DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2013-01-01"), 
					DayWrapper.makeDay(
						1, 
						PlainDoseWrapper.makeDose(new BigDecimal(0.5)), 
						PlainDoseWrapper.makeDose(new BigDecimal(0.5))))));
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011, forløbet gentages efter 5 dage, og ophører tirsdag den 1. januar 2013:\n"+
				"   Doseringsforløb:\n"+
				"   Lørdag den 1. januar 2011: 0,5 stk 2 gange.\n   Bemærk: ved måltid",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"RepeatedConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"1/2 stk 2 gange samme dag hver 5. dag.\n   Bemærk: ved måltid", 
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				1/5., 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001); 				
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));				
	}
	
	@Test
	public void test1stkDagligKl0800() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					1, null, DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-30"), 
					DayWrapper.makeDay(
						1, 
						TimedDoseWrapper.makeDose(new LocalTime(8,00), new BigDecimal(1), false)))));
		Assert.assertEquals(
				"Doseringsforløbet starter lørdag den 1. januar 2011, gentages hver dag, og ophører søndag den 30. januar 2011:\n"+
				"   Doseringsforløb:\n"+
				"   1 stk kl. 08:00",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"RepeatedConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"1 stk kl. 08:00 daglig", 
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				1.0, 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001);
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));				
	}

    @Test
    public void test1stkHver6uger() {
        DosageWrapper dosage = DosageWrapper.makeDosage(
                StructuresWrapper.makeStructures(
                        UnitOrUnitsWrapper.makeUnit("stk"),
                        StructureWrapper.makeStructure(
                                42, null, DateOrDateTimeWrapper.makeDateTime("2014-02-07 07:19:00"), null,
                                DayWrapper.makeDay(1,
                                        PlainDoseWrapper.makeDose(new BigDecimal(1)))
                        )
                ));


        Assert.assertEquals("Doseringsforløbet starter fredag den 7. februar 2014 kl. 07:19, forløbet gentages efter 42 dage:\n" +
                "   Doseringsforløb:\n" +
                "   Fredag den 7. februar 2014: 1 stk", DosisTilTekstWrapper.convertLongText(dosage));
        Assert.assertEquals(
                "1 stk fredag hver 6. uge",
                DosisTilTekstWrapper.convertShortText(dosage));
        Assert.assertEquals(
                0.023809524,
                DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(),
                0.000000001);
        Assert.assertEquals(DosageType.Fixed, DosisTilTekstWrapper.getDosageType(dosage));
    }

    @Test
    public void test1stkKl12Hver6uger() {
        DosageWrapper dosage = DosageWrapper.makeDosage(
                StructuresWrapper.makeStructures(
                        UnitOrUnitsWrapper.makeUnit("stk"),
                        StructureWrapper.makeStructure(
                                42, null, DateOrDateTimeWrapper.makeDateTime("2014-02-07 07:19:00"), null,
                                DayWrapper.makeDay(1,
                                        TimedDoseWrapper.makeDose(new LocalTime(12,0), new BigDecimal(1), false))
                        )
                ));


        Assert.assertEquals("Doseringsforløbet starter fredag den 7. februar 2014 kl. 07:19, forløbet gentages efter 42 dage:\n" +
                "   Doseringsforløb:\n" +
                "   Fredag den 7. februar 2014: 1 stk kl. 12:00", DosisTilTekstWrapper.convertLongText(dosage));
        Assert.assertEquals(
                "1 stk kl. 12:00 fredag hver 6. uge",
                DosisTilTekstWrapper.convertShortText(dosage));
        Assert.assertEquals(
                0.023809524,
                DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(),
                0.000000001);
        Assert.assertEquals(DosageType.Fixed, DosisTilTekstWrapper.getDosageType(dosage));
    }

    @Test
    public void test1stkMiddagHver6uger() {
        DosageWrapper dosage = DosageWrapper.makeDosage(
                StructuresWrapper.makeStructures(
                        UnitOrUnitsWrapper.makeUnit("stk"),
                        StructureWrapper.makeStructure(
                                42, null, DateOrDateTimeWrapper.makeDateTime("2014-02-07 07:19:00"), null,
                                DayWrapper.makeDay(1,
                                        NoonDoseWrapper.makeDose(new BigDecimal(1)))
                        )
                ));


        Assert.assertEquals("Doseringsforløbet starter fredag den 7. februar 2014 kl. 07:19, forløbet gentages efter 42 dage:\n" +
                "   Doseringsforløb:\n" +
                "   Fredag den 7. februar 2014: 1 stk middag", DosisTilTekstWrapper.convertLongText(dosage));
        Assert.assertEquals(
                "1 stk middag fredag hver 6. uge",
                DosisTilTekstWrapper.convertShortText(dosage));
        Assert.assertEquals(
                0.023809524,
                DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(),
                0.000000001);
        Assert.assertEquals(DosageType.Fixed, DosisTilTekstWrapper.getDosageType(dosage));
    }

    @Test
    public void test1stkMorgenOgNatHver6uger() {
        DosageWrapper dosage = DosageWrapper.makeDosage(
                StructuresWrapper.makeStructures(
                        UnitOrUnitsWrapper.makeUnit("stk"),
                        StructureWrapper.makeStructure(
                                42, null, DateOrDateTimeWrapper.makeDateTime("2014-02-07 07:19:00"), null,
                                DayWrapper.makeDay(1,
                                        NoonDoseWrapper.makeDose(new BigDecimal(1)),
                                        NightDoseWrapper.makeDose(new BigDecimal(1)))
                        )
                ));


        Assert.assertEquals("Doseringsforløbet starter fredag den 7. februar 2014 kl. 07:19, forløbet gentages efter 42 dage:\n" +
                "   Doseringsforløb:\n" +
                "   Fredag den 7. februar 2014: 1 stk middag + 1 stk nat", DosisTilTekstWrapper.convertLongText(dosage));
        Assert.assertNull(DosisTilTekstWrapper.convertShortText(dosage)); // does not have a short text translation
        Assert.assertEquals(
                0.047619048,
                DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(),
                0.000000001);
        Assert.assertEquals(DosageType.Fixed, DosisTilTekstWrapper.getDosageType(dosage));
    }
}
