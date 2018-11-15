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
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.EveningDoseWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.MorningDoseWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.NightDoseWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.NoonDoseWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.StructureWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.StructuresWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.UnitOrUnitsWrapper;

public class MorningNoonEveningNightConverterTest extends DosisTilTekstWrapperTestBase  {
	
	@Test
	public void testMorningNoonEveningAndNight() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					1, "!", DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-30"),
					DayWrapper.makeDay(
						1, 
						MorningDoseWrapper.makeDose(new BigDecimal(1)), 
						NoonDoseWrapper.makeDose(new BigDecimal(2)), 
						EveningDoseWrapper.makeDose(new BigDecimal(3)), 
						NightDoseWrapper.makeDose(new BigDecimal(4)))))); 
		Assert.assertEquals(
			"Doseringsforløbet starter lørdag den 1. januar 2011, gentages hver dag, og ophører søndag den 30. januar 2011:\n"+
			"   Doseringsforløb:\n"+
			"   1 stk morgen + 2 stk middag + 3 stk aften + 4 stk nat.\n   Bemærk: !",
			DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
			"MorningNoonEveningNightConverterImpl", 
			DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
			"1 stk morgen, 2 stk middag, 3 stk aften og 4 stk nat !", 
			DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
			10.0, 
			DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
			0.000000001); 		
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));		
	}

	@Test
	public void testMorningNoonEveningAndNightWithEqualDoses() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					1, "ved måltid", DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-30"),
					DayWrapper.makeDay(
						1, 
						MorningDoseWrapper.makeDose(new BigDecimal(2)), 
						NoonDoseWrapper.makeDose(new BigDecimal(2)), 
						EveningDoseWrapper.makeDose(new BigDecimal(2)), 
						NightDoseWrapper.makeDose(new BigDecimal(2)))))); 
		Assert.assertEquals(
			"Doseringsforløbet starter lørdag den 1. januar 2011, gentages hver dag, og ophører søndag den 30. januar 2011:\n"+
			"   Doseringsforløb:\n"+
			"   2 stk morgen + 2 stk middag + 2 stk aften + 2 stk nat.\n   Bemærk: ved måltid",
			DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
			"MorningNoonEveningNightConverterImpl", 
			DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
			"2 stk morgen, middag, aften og nat ved måltid", 
			DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				8.0, 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001); 			
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));		
	}
	
	@Test
	public void testNoonEveningAndNight() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					1, "ved måltid", DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-30"),
					DayWrapper.makeDay(
						1, 
						NoonDoseWrapper.makeDose(new BigDecimal(2)), 
						EveningDoseWrapper.makeDose(new BigDecimal(3)), 
						NightDoseWrapper.makeDose(new BigDecimal(4)))))); 
		Assert.assertEquals(
			"Doseringsforløbet starter lørdag den 1. januar 2011, gentages hver dag, og ophører søndag den 30. januar 2011:\n"+
			"   Doseringsforløb:\n"+
			"   2 stk middag + 3 stk aften + 4 stk nat.\n   Bemærk: ved måltid",
			DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
			"MorningNoonEveningNightConverterImpl", 
			DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
			"2 stk middag, 3 stk aften og 4 stk nat ved måltid", 
			DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				9.0, 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001); 			
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));		
	}
	
	@Test
	public void testMorningNoonAndEvening() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					1, "ved måltid", DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-30"),
					DayWrapper.makeDay(
						1, 
						MorningDoseWrapper.makeDose(new BigDecimal(1)), 
						NoonDoseWrapper.makeDose(new BigDecimal(2)), 
						EveningDoseWrapper.makeDose(new BigDecimal(3)))))); 
		Assert.assertEquals(
			"Doseringsforløbet starter lørdag den 1. januar 2011, gentages hver dag, og ophører søndag den 30. januar 2011:\n"+
			"   Doseringsforløb:\n"+
			"   1 stk morgen + 2 stk middag + 3 stk aften.\n   Bemærk: ved måltid",
			DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"MorningNoonEveningNightConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
			"1 stk morgen, 2 stk middag og 3 stk aften ved måltid", 
			DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				6.0, 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001); 			
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));		
	}

	@Test
	public void testMorningAndNoonWithZeroIntervals() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					1, "ved måltid", DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-30"),
					DayWrapper.makeDay(
						1, 
						MorningDoseWrapper.makeDose(new BigDecimal(0), new BigDecimal(1)), 
						NoonDoseWrapper.makeDose(new BigDecimal(2), new BigDecimal(3)))))); 
		Assert.assertEquals(
			"Doseringsforløbet starter lørdag den 1. januar 2011, gentages hver dag, og ophører søndag den 30. januar 2011:\n"+
			"   Doseringsforløb:\n"+
			"   0-1 stk morgen + 2-3 stk middag.\n   Bemærk: ved måltid",
			DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"MorningNoonEveningNightConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
			"0-1 stk morgen og 2-3 stk middag ved måltid", 
			DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				2, 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getInterval().getMinimum().doubleValue(), 
				0.000000001); 				
		Assert.assertEquals(
				4, 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getInterval().getMaximum().doubleValue(), 
				0.000000001);
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));		
	}
	
	public void testNight() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("stk"), 
				StructureWrapper.makeStructure(
					1, "ved måltid", DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-30"),
					DayWrapper.makeDay(
						1, 
						NightDoseWrapper.makeDose(new BigDecimal(2)))))); 				
		Assert.assertEquals(
			"Daglig 2 stk nat ved måltid", 
			DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"MorningNoonEveningNightConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
			"2 stk nat ved måltid", 
			DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				2.0, 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001); 		
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));		
	}

	@Test
	public void test1DråbeMiddagOgAften() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("dråber"), 
				StructureWrapper.makeStructure(
					1, null, DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-30"),
					DayWrapper.makeDay(
						1, 
						NoonDoseWrapper.makeDose(new BigDecimal(1)), 
						EveningDoseWrapper.makeDose(new BigDecimal(1)))))); 				
		Assert.assertEquals(
			"Doseringsforløbet starter lørdag den 1. januar 2011, gentages hver dag, og ophører søndag den 30. januar 2011:\n"+
			"   Doseringsforløb:\n"+
			"   1 dråbe middag + 1 dråbe aften",
			DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"MorningNoonEveningNightConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
			"1 dråbe middag og aften", 
			DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				2.0, 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001); 				
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));
	}

	@Test
	public void test1DråbeAftenOgNat() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("dråber"), 
				StructureWrapper.makeStructure(
					1, null, DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-30"),
					DayWrapper.makeDay(
						1, 
						EveningDoseWrapper.makeDose(new BigDecimal(1)), 
						NightDoseWrapper.makeDose(new BigDecimal(1)))))); 				
		Assert.assertEquals(
			"Doseringsforløbet starter lørdag den 1. januar 2011, gentages hver dag, og ophører søndag den 30. januar 2011:\n"+
			"   Doseringsforløb:\n"+
			"   1 dråbe aften + 1 dråbe nat",
			DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"MorningNoonEveningNightConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
			"1 dråbe aften og nat", 
			DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				2.0, 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001); 				
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));		
	}

	@Test
	public void test1DråbeNat() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("dråber"), 
				StructureWrapper.makeStructure(
					1, null, DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-30"),
					DayWrapper.makeDay(
						1, 
						NightDoseWrapper.makeDose(new BigDecimal(1)))))); 				
		Assert.assertEquals(
			"Doseringsforløbet starter lørdag den 1. januar 2011, gentages hver dag, og ophører søndag den 30. januar 2011:\n"+
			"   Doseringsforløb:\n"+
			"   1 dråbe nat",
			DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"MorningNoonEveningNightConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
			"1 dråbe nat", 
			DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				1.0, 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001); 				
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));		
	}

	@Test
	public void test400MilligramNat() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("milligram"), 
				StructureWrapper.makeStructure(
					1, null, DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-30"),
					DayWrapper.makeDay(
						1, 
						NightDoseWrapper.makeDose(new BigDecimal(400.000)))))); 				
		Assert.assertEquals(
			"Doseringsforløbet starter lørdag den 1. januar 2011, gentages hver dag, og ophører søndag den 30. januar 2011:\n"+
			"   Doseringsforløb:\n"+
			"   400 milligram nat",
			DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"MorningNoonEveningNightConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
			"400 milligram nat", 
			DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				400.0, 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001); 				
		Assert.assertEquals(DosageType.Temporary, DosisTilTekstWrapper.getDosageType(dosage));		
	}
	
	@Test /* handle plurals, see https://jira.trifork.com/browse/FMK-943 */
	public void testJiraFMK943() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("tablet"), 
				StructureWrapper.makeStructure(
					1, null, DateOrDateTimeWrapper.makeDateTime("2012-06-26 00:00:00"), null, 
					DayWrapper.makeDay(
						1, 
						MorningDoseWrapper.makeDose(new BigDecimal(1)), 
						EveningDoseWrapper.makeDose(new BigDecimal(2))))));
		Assert.assertEquals(
				"Doseringsforløbet starter tirsdag den 26. juni 2012 kl. 00:00 og gentages hver dag:\n"+
				"   Doseringsforløb:\n"+
				"   1 tablet morgen + 2 tabletter aften",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"MorningNoonEveningNightConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"1 tablet morgen og 2 tabletter aften", 
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				3.0, 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001); 				
		Assert.assertEquals(DosageType.Fixed, DosisTilTekstWrapper.getDosageType(dosage));		
	}	

	@Test /* handle plurals, see https://jira.trifork.com/browse/FMK-943 */
	public void testJiraFMK943Variant() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("tablet"), 
				StructureWrapper.makeStructure(
					1, null, DateOrDateTimeWrapper.makeDateTime("2012-06-26 00:00:00"), null, 
					DayWrapper.makeDay(
						1, 
						MorningDoseWrapper.makeDose(new BigDecimal(0), new BigDecimal(1)), 
						EveningDoseWrapper.makeDose(new BigDecimal(0.5), new BigDecimal(1.5))))));
		Assert.assertEquals(
				"Doseringsforløbet starter tirsdag den 26. juni 2012 kl. 00:00 og gentages hver dag:\n"+
				"   Doseringsforløb:\n"+
				"   0-1 tablet morgen + 0,5-1,5 tabletter aften",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"MorningNoonEveningNightConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"0-1 tablet morgen og 1/2-1 1/2 tabletter aften", 
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				0.5, 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getInterval().getMinimum().doubleValue(), 
				0.000000001); 				
		Assert.assertEquals(
				2.5, 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getInterval().getMaximum().doubleValue(), 
				0.000000001); 				
		Assert.assertEquals(DosageType.Fixed, DosisTilTekstWrapper.getDosageType(dosage));		
	}	

	@Test /* handle zero dosages stored in the database using the 2008-namespace, see https://jira.trifork.com/browse/FMK-872 */
	public void testJiraFMK872() throws Exception {
		DosageWrapper dosage = DosageWrapper.makeDosage(
			StructuresWrapper.makeStructures(
				UnitOrUnitsWrapper.makeUnit("tablet"), 
				StructureWrapper.makeStructure(
					1, null, DateOrDateTimeWrapper.makeDateTime("2012-06-26 00:00:00"), null, 
					DayWrapper.makeDay(
						1, 
						MorningDoseWrapper.makeDose(new BigDecimal(0)), 
						NoonDoseWrapper.makeDose(new BigDecimal("42.117")),
						EveningDoseWrapper.makeDose(new BigDecimal(0.0), new BigDecimal(0.0))))));
		Assert.assertEquals(
				"Doseringsforløbet starter tirsdag den 26. juni 2012 kl. 00:00 og gentages hver dag:\n"+
				"   Doseringsforløb:\n"+
				"   42,117 tabletter middag + 0-0 tabletter aften",
				DosisTilTekstWrapper.convertLongText(dosage));
		Assert.assertEquals(
				"MorningNoonEveningNightConverterImpl", 
				DosisTilTekstWrapper.getShortTextConverterClassName(dosage));
		Assert.assertEquals(
				"42,117 tabletter middag og 0-0 tabletter aften", 
				DosisTilTekstWrapper.convertShortText(dosage));
		Assert.assertEquals(
				42.117, 
				DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(), 
				0.000000001); 				
		Assert.assertEquals(DosageType.Fixed, DosisTilTekstWrapper.getDosageType(dosage));		
	}	
	

    @Test
    public void testMorningFixedNightPN() {
        DosageWrapper dosage = DosageWrapper.makeDosage(
            StructuresWrapper.makeStructures(
                UnitOrUnitsWrapper.makeUnit("tablet"), 
                StructureWrapper.makeStructure(
                    1, null, DateOrDateTimeWrapper.makeDateTime("2012-06-26 00:00:00"), null, 
                    DayWrapper.makeDay(
                        1, 
                        MorningDoseWrapper.makeDose(new BigDecimal(1)), 
                        null,
                        null,
                        NightDoseWrapper.makeDose(new BigDecimal(1), true)
                    ))));
        
        String longText = DosisTilTekstWrapper.convertLongText(dosage);
        String shortText = DosisTilTekstWrapper.convertShortText(dosage);
        Assert.assertEquals("1 tablet morgen, 1 tablet nat efter behov", shortText);
        Assert.assertTrue(longText.contains("1 tablet morgen + 1 tablet nat efter behov"));
    }
    
    @Test
    public void testMorningPnNightFixed() {
        DosageWrapper dosage = DosageWrapper.makeDosage(
            StructuresWrapper.makeStructures(
                UnitOrUnitsWrapper.makeUnit("tablet"), 
                StructureWrapper.makeStructure(
                    1, null, DateOrDateTimeWrapper.makeDateTime("2012-06-26 00:00:00"), null, 
                    DayWrapper.makeDay(
                        1, 
                        MorningDoseWrapper.makeDose(new BigDecimal(1), true), 
                        null,
                        null,
                        NightDoseWrapper.makeDose(new BigDecimal(1))
                    ))));
        
        String longText = DosisTilTekstWrapper.convertLongText(dosage);
        String shortText = DosisTilTekstWrapper.convertShortText(dosage);
        Assert.assertEquals("1 tablet morgen efter behov, 1 tablet nat", shortText);
        Assert.assertTrue(longText.contains("1 tablet morgen efter behov + 1 tablet nat"));
    }
    

    @Test
    public void testMorningPnNoonFixedNightFixed() {
        DosageWrapper dosage = DosageWrapper.makeDosage(
            StructuresWrapper.makeStructures(
                UnitOrUnitsWrapper.makeUnit("tablet"), 
                StructureWrapper.makeStructure(
                    1, null, DateOrDateTimeWrapper.makeDateTime("2012-06-26 00:00:00"), null, 
                    DayWrapper.makeDay(
                        1, 
                        MorningDoseWrapper.makeDose(new BigDecimal(1), true), 
                        NoonDoseWrapper.makeDose(new BigDecimal(1), true),
                        EveningDoseWrapper.makeDose(new BigDecimal(1), true),
                        NightDoseWrapper.makeDose(new BigDecimal(1), true)
                    ))));
        
        String longText = DosisTilTekstWrapper.convertLongText(dosage);
        String shortText = DosisTilTekstWrapper.convertShortText(dosage);
        Assert.assertNull(shortText); // Skulle have været "1 tablet morgen efter behov, 1 tablet middag efter behov, 1 tablet aften efter behov, 1 tablet nat efter behov" men for lang    
    }
    
    @Test
    public void testMorningNoonEveningNightFixed() {
        DosageWrapper dosage = DosageWrapper.makeDosage(
            StructuresWrapper.makeStructures(
                UnitOrUnitsWrapper.makeUnit("tablet"), 
                StructureWrapper.makeStructure(
                    1, null, DateOrDateTimeWrapper.makeDateTime("2012-06-26 00:00:00"), null, 
                    DayWrapper.makeDay(
                        1, 
                        MorningDoseWrapper.makeDose(new BigDecimal(1)), 
                        NoonDoseWrapper.makeDose(new BigDecimal(1)),
                        EveningDoseWrapper.makeDose(new BigDecimal(1)),
                        NightDoseWrapper.makeDose(new BigDecimal(1))
                    ))));
        
        String longText = DosisTilTekstWrapper.convertLongText(dosage);
        String shortText = DosisTilTekstWrapper.convertShortText(dosage);
        Assert.assertEquals("1 tablet morgen, middag, aften og nat", shortText);
    }
    
    
    
    @Test
    public void testMorningFixedNightPNDifferentQuantities() {
        DosageWrapper dosage = DosageWrapper.makeDosage(
            StructuresWrapper.makeStructures(
                UnitOrUnitsWrapper.makeUnit("tablet"), 
                StructureWrapper.makeStructure(
                    1, null, DateOrDateTimeWrapper.makeDateTime("2012-06-26 00:00:00"), null, 
                    DayWrapper.makeDay(
                        1, 
                        MorningDoseWrapper.makeDose(new BigDecimal(1)), 
                        null,
                        null,
                        NightDoseWrapper.makeDose(new BigDecimal(2), true)
                    ))));
        
        String longText = DosisTilTekstWrapper.convertLongText(dosage);
        String shortText = DosisTilTekstWrapper.convertShortText(dosage);
        Assert.assertEquals("1 tablet morgen, 2 tabletter nat efter behov", shortText);
        Assert.assertTrue(longText.contains("1 tablet morgen + 2 tabletter nat efter behov"));

    }
}
