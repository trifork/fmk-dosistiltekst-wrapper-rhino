package dk.medicinkortet.fmkdosistiltekstwrapper.ns2009;

import dk.medicinkortet.fmkdosistiltekstwrapper.*;
import dk.medicinkortet.fmkdosistiltekstwrapper.rhino.DosisTilTekstWrapper;
import dk.medicinkortet.fmkdosistiltekstwrapper.vowrapper.*;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class NumberOfWholeWeeksConverterTest extends DosisTilTekstWrapperTestBase  {

    @Test
    public void test1stkMorgen1Uge1UgesPause() {
        DosageWrapper dosage = DosageWrapper.makeDosage(
                StructuresWrapper.makeStructures(
                        UnitOrUnitsWrapper.makeUnit("stk"),
                        StructureWrapper.makeStructure(
                                14, "ved måltid", DateOrDateTimeWrapper.makeDateTime("2014-02-07 07:19:00"), null,
                                DayWrapper.makeDay(1, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(2, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(3, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(4, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(5, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(6, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(7, NoonDoseWrapper.makeDose(new BigDecimal(1)))
                        )
                ));

        Assert.assertEquals("Doseringsforløbet starter fredag den 7. februar 2014 kl. 07:19, forløbet gentages efter 14 dage:\n" +
        		 "   Doseringsforløb:\n" +
                 "   Fredag den 7. februar 2014: 1 stk middag\n" +
                 "   Lørdag den 8. februar 2014: 1 stk middag\n" +
                 "   Søndag den 9. februar 2014: 1 stk middag\n" +
                 "   Mandag den 10. februar 2014: 1 stk middag\n" +
                 "   Tirsdag den 11. februar 2014: 1 stk middag\n" +
                 "   Onsdag den 12. februar 2014: 1 stk middag\n" +
                 "   Torsdag den 13. februar 2014: 1 stk middag.\n"+
				 "   Bemærk: ved måltid", DosisTilTekstWrapper.convertLongText(dosage));
        Assert.assertNull(DosisTilTekstWrapper.convertShortText(dosage));
        Assert.assertEquals("1 stk middag daglig i en uge, herefter en uges pause.\nBemærk: ved måltid", DosisTilTekstWrapper.convertShortText(dosage, 100));
        Assert.assertEquals(
                0.5,
                DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(),
                0.000000001);
        Assert.assertEquals(DosageType.Fixed, DosisTilTekstWrapper.getDosageType(dosage));
    }

    @Test
    public void test1stkMorgenOgAften1Uge1UgesPause_IngenKortTekst() {
        DosageWrapper dosage = DosageWrapper.makeDosage(
                StructuresWrapper.makeStructures(
                        UnitOrUnitsWrapper.makeUnit("stk"),
                        StructureWrapper.makeStructure(
                                14, null, DateOrDateTimeWrapper.makeDateTime("2014-02-07 07:19:00"), null,
                                DayWrapper.makeDay(1, NoonDoseWrapper.makeDose(new BigDecimal(1)), EveningDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(2, NoonDoseWrapper.makeDose(new BigDecimal(1)), EveningDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(3, NoonDoseWrapper.makeDose(new BigDecimal(1)), EveningDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(4, NoonDoseWrapper.makeDose(new BigDecimal(1)), EveningDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(5, NoonDoseWrapper.makeDose(new BigDecimal(1)), EveningDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(6, NoonDoseWrapper.makeDose(new BigDecimal(1)), EveningDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(7, NoonDoseWrapper.makeDose(new BigDecimal(1)), EveningDoseWrapper.makeDose(new BigDecimal(1)))
                        )
                ));

        Assert.assertEquals("Doseringsforløbet starter fredag den 7. februar 2014 kl. 07:19, forløbet gentages efter 14 dage:\n" +
                "   Doseringsforløb:\n" +
                "   Fredag den 7. februar 2014: 1 stk middag + 1 stk aften\n" +
                "   Lørdag den 8. februar 2014: 1 stk middag + 1 stk aften\n" +
                "   Søndag den 9. februar 2014: 1 stk middag + 1 stk aften\n" +
                "   Mandag den 10. februar 2014: 1 stk middag + 1 stk aften\n" +
                "   Tirsdag den 11. februar 2014: 1 stk middag + 1 stk aften\n" +
                "   Onsdag den 12. februar 2014: 1 stk middag + 1 stk aften\n" +
                "   Torsdag den 13. februar 2014: 1 stk middag + 1 stk aften", DosisTilTekstWrapper.convertLongText(dosage));
        Assert.assertNull(DosisTilTekstWrapper.convertShortText(dosage));
        Assert.assertEquals(
                1.0,
                DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(),
                0.000000001);
        Assert.assertEquals(DosageType.Fixed, DosisTilTekstWrapper.getDosageType(dosage));
    }

    @Test
    public void test1stk2GangeDaglig1Uge1UgesPause() {
        DosageWrapper dosage = DosageWrapper.makeDosage(
                StructuresWrapper.makeStructures(
                        UnitOrUnitsWrapper.makeUnit("stk"),
                        StructureWrapper.makeStructure(
                                14, null, DateOrDateTimeWrapper.makeDateTime("2014-02-07 07:19:00"), null,
                                DayWrapper.makeDay(1, PlainDoseWrapper.makeDose(new BigDecimal(1)), PlainDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(2, PlainDoseWrapper.makeDose(new BigDecimal(1)), PlainDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(3, PlainDoseWrapper.makeDose(new BigDecimal(1)), PlainDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(4, PlainDoseWrapper.makeDose(new BigDecimal(1)), PlainDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(5, PlainDoseWrapper.makeDose(new BigDecimal(1)), PlainDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(6, PlainDoseWrapper.makeDose(new BigDecimal(1)), PlainDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(7, PlainDoseWrapper.makeDose(new BigDecimal(1)), PlainDoseWrapper.makeDose(new BigDecimal(1)))
                        )
                ));

        Assert.assertEquals("Doseringsforløbet starter fredag den 7. februar 2014 kl. 07:19, forløbet gentages efter 14 dage:\n" +
                "   Doseringsforløb:\n" +
                "   Fredag den 7. februar 2014: 1 stk 2 gange\n" +
                "   Lørdag den 8. februar 2014: 1 stk 2 gange\n" +
                "   Søndag den 9. februar 2014: 1 stk 2 gange\n" +
                "   Mandag den 10. februar 2014: 1 stk 2 gange\n" +
                "   Tirsdag den 11. februar 2014: 1 stk 2 gange\n" +
                "   Onsdag den 12. februar 2014: 1 stk 2 gange\n" +
                "   Torsdag den 13. februar 2014: 1 stk 2 gange", DosisTilTekstWrapper.convertLongText(dosage));
        Assert.assertEquals("1 stk 2 gange daglig i en uge, herefter en uges pause", DosisTilTekstWrapper.convertShortText(dosage));
        Assert.assertEquals(
                1.0,
                DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(),
                0.000000001);
        Assert.assertEquals(DosageType.Fixed, DosisTilTekstWrapper.getDosageType(dosage));
    }

    @Test
    public void test1stkMorgen1Uge5UgersPause() {
        DosageWrapper dosage = DosageWrapper.makeDosage(
                StructuresWrapper.makeStructures(
                        UnitOrUnitsWrapper.makeUnit("stk"),
                        StructureWrapper.makeStructure(
                                42, null, DateOrDateTimeWrapper.makeDateTime("2014-02-07 07:19:00"), null,
                                DayWrapper.makeDay(1, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(2, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(3, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(4, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(5, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(6, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(7, NoonDoseWrapper.makeDose(new BigDecimal(1)))
                        )
                ));

        Assert.assertEquals("Doseringsforløbet starter fredag den 7. februar 2014 kl. 07:19, forløbet gentages efter 42 dage:\n" +
                "   Doseringsforløb:\n" +
                "   Fredag den 7. februar 2014: 1 stk middag\n" +
                "   Lørdag den 8. februar 2014: 1 stk middag\n" +
                "   Søndag den 9. februar 2014: 1 stk middag\n" +
                "   Mandag den 10. februar 2014: 1 stk middag\n" +
                "   Tirsdag den 11. februar 2014: 1 stk middag\n" +
                "   Onsdag den 12. februar 2014: 1 stk middag\n" +
                "   Torsdag den 13. februar 2014: 1 stk middag", DosisTilTekstWrapper.convertLongText(dosage));
        Assert.assertEquals("1 stk middag daglig i en uge, herefter 5 ugers pause", DosisTilTekstWrapper.convertShortText(dosage));
        Assert.assertEquals(
                0.166666667,
                DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(),
                0.000000001);
        Assert.assertEquals(DosageType.Fixed, DosisTilTekstWrapper.getDosageType(dosage));
    }

    @Test
    public void test1stkMorgen2Uge4UgersPause() {
        DosageWrapper dosage = DosageWrapper.makeDosage(
                StructuresWrapper.makeStructures(
                        UnitOrUnitsWrapper.makeUnit("stk"),
                        StructureWrapper.makeStructure(
                                42, null, DateOrDateTimeWrapper.makeDateTime("2014-02-07 07:19:00"), null,
                                DayWrapper.makeDay(1, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(2, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(3, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(4, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(5, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(6, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(7, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(8, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(9, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(10, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(11, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(12, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(13, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(14, NoonDoseWrapper.makeDose(new BigDecimal(1)))
                        )
                ));

        Assert.assertEquals("Doseringsforløbet starter fredag den 7. februar 2014 kl. 07:19, forløbet gentages efter 42 dage:\n" +
                "   Doseringsforløb:\n" +
                "   Fredag den 7. februar 2014: 1 stk middag\n" +
                "   Lørdag den 8. februar 2014: 1 stk middag\n" +
                "   Søndag den 9. februar 2014: 1 stk middag\n" +
                "   Mandag den 10. februar 2014: 1 stk middag\n" +
                "   Tirsdag den 11. februar 2014: 1 stk middag\n" +
                "   Onsdag den 12. februar 2014: 1 stk middag\n" +
                "   Torsdag den 13. februar 2014: 1 stk middag\n" +
                "   Fredag den 14. februar 2014: 1 stk middag\n" +
                "   Lørdag den 15. februar 2014: 1 stk middag\n" +
                "   Søndag den 16. februar 2014: 1 stk middag\n" +
                "   Mandag den 17. februar 2014: 1 stk middag\n" +
                "   Tirsdag den 18. februar 2014: 1 stk middag\n" +
                "   Onsdag den 19. februar 2014: 1 stk middag\n" +
                "   Torsdag den 20. februar 2014: 1 stk middag", DosisTilTekstWrapper.convertLongText(dosage));
        Assert.assertEquals("1 stk middag daglig i 2 uger, herefter 4 ugers pause", DosisTilTekstWrapper.convertShortText(dosage));
        Assert.assertEquals(
                0.333333333,
                DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(),
                0.000000001);
        Assert.assertEquals(DosageType.Fixed, DosisTilTekstWrapper.getDosageType(dosage));
    }

    @Test
    public void test1stkMorgen2UgeIngenPause() {
        // This is really abuse og a bug structure
        // Equivalent with one day and iteration=1.
        DosageWrapper dosage = DosageWrapper.makeDosage(
                StructuresWrapper.makeStructures(
                        UnitOrUnitsWrapper.makeUnit("stk"),
                        StructureWrapper.makeStructure(
                                14, null, DateOrDateTimeWrapper.makeDateTime("2014-02-07 07:19:00"), null,
                                DayWrapper.makeDay(1, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(2, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(3, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(4, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(5, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(6, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(7, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(8, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(9, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(10, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(11, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(12, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(13, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(14, NoonDoseWrapper.makeDose(new BigDecimal(1)))
                        )
                ));

        Assert.assertEquals("Doseringsforløbet starter fredag den 7. februar 2014 kl. 07:19, forløbet gentages efter 14 dage:\n" +
                "   Doseringsforløb:\n" +
                "   Fredag den 7. februar 2014: 1 stk middag\n" +
                "   Lørdag den 8. februar 2014: 1 stk middag\n" +
                "   Søndag den 9. februar 2014: 1 stk middag\n" +
                "   Mandag den 10. februar 2014: 1 stk middag\n" +
                "   Tirsdag den 11. februar 2014: 1 stk middag\n" +
                "   Onsdag den 12. februar 2014: 1 stk middag\n" +
                "   Torsdag den 13. februar 2014: 1 stk middag\n" +
                "   Fredag den 14. februar 2014: 1 stk middag\n" +
                "   Lørdag den 15. februar 2014: 1 stk middag\n" +
                "   Søndag den 16. februar 2014: 1 stk middag\n" +
                "   Mandag den 17. februar 2014: 1 stk middag\n" +
                "   Tirsdag den 18. februar 2014: 1 stk middag\n" +
                "   Onsdag den 19. februar 2014: 1 stk middag\n" +
                "   Torsdag den 20. februar 2014: 1 stk middag", DosisTilTekstWrapper.convertLongText(dosage));
        Assert.assertEquals("1 stk middag daglig", DosisTilTekstWrapper.convertShortText(dosage));
        Assert.assertEquals(
                1.0,
                DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(),
                0.000000001);
        Assert.assertEquals(DosageType.Fixed, DosisTilTekstWrapper.getDosageType(dosage));
    }

    @Test
    public void test1stkMorgen3dageHver6uger() {
        DosageWrapper dosage = DosageWrapper.makeDosage(
                StructuresWrapper.makeStructures(
                        UnitOrUnitsWrapper.makeUnit("stk"),
                        StructureWrapper.makeStructure(
                                42, null, DateOrDateTimeWrapper.makeDateTime("2014-02-07 07:19:00"), null,
                                DayWrapper.makeDay(1, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(2, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(3, NoonDoseWrapper.makeDose(new BigDecimal(1)))
                        )
                ));

        Assert.assertEquals("Doseringsforløbet starter fredag den 7. februar 2014 kl. 07:19, forløbet gentages efter 42 dage:\n" +
                "   Doseringsforløb:\n" +
                "   Fredag den 7. februar 2014: 1 stk middag\n" +
                "   Lørdag den 8. februar 2014: 1 stk middag\n" +
                "   Søndag den 9. februar 2014: 1 stk middag", DosisTilTekstWrapper.convertLongText(dosage));
        Assert.assertEquals("1 stk middag daglig i 3 dage, herefter 39 dages pause", DosisTilTekstWrapper.convertShortText(dosage));
        Assert.assertEquals(
                0.071428571,
                DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(),
                0.000000001);
        Assert.assertEquals(DosageType.Fixed, DosisTilTekstWrapper.getDosageType(dosage));
    }

    @Test
    public void test1stkMorgenDag135Hver6uger_IngenKortTekst() {
        DosageWrapper dosage = DosageWrapper.makeDosage(
                StructuresWrapper.makeStructures(
                        UnitOrUnitsWrapper.makeUnit("stk"),
                        StructureWrapper.makeStructure(
                                42, null, DateOrDateTimeWrapper.makeDateTime("2014-02-07 07:19:00"), null,
                                DayWrapper.makeDay(1, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(3, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(5, NoonDoseWrapper.makeDose(new BigDecimal(1)))
                        )
                ));

        Assert.assertEquals("Doseringsforløbet starter fredag den 7. februar 2014 kl. 07:19, forløbet gentages efter 42 dage.\n" +
                "Bemærk at doseringen har et komplekst forløb:\n" +
                "   Doseringsforløb:\n" +
                "   Fredag den 7. februar 2014: 1 stk middag\n" +
                "   Søndag den 9. februar 2014: 1 stk middag\n" +
                "   Tirsdag den 11. februar 2014: 1 stk middag", DosisTilTekstWrapper.convertLongText(dosage));
        Assert.assertNull(DosisTilTekstWrapper.convertShortText(dosage));
        Assert.assertEquals(
                0.071428571,
                DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(),
                0.000000001);
        Assert.assertEquals(DosageType.Fixed, DosisTilTekstWrapper.getDosageType(dosage));
    }

    @Test
    public void test1stkMorgenDag246Hver6uger_IngenKortTekst() {
        DosageWrapper dosage = DosageWrapper.makeDosage(
                StructuresWrapper.makeStructures(
                        UnitOrUnitsWrapper.makeUnit("stk"),
                        StructureWrapper.makeStructure(
                                42, null, DateOrDateTimeWrapper.makeDateTime("2014-02-07 07:19:00"), null,
                                DayWrapper.makeDay(2, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(4, NoonDoseWrapper.makeDose(new BigDecimal(1))),
                                DayWrapper.makeDay(6, NoonDoseWrapper.makeDose(new BigDecimal(1)))
                        )
                ));

        Assert.assertEquals("Doseringsforløbet starter fredag den 7. februar 2014 kl. 07:19, forløbet gentages efter 42 dage.\n" +
                "Bemærk at doseringen har et komplekst forløb:\n" +
                "   Doseringsforløb:\n" +
                "   Lørdag den 8. februar 2014: 1 stk middag\n" +
                "   Mandag den 10. februar 2014: 1 stk middag\n" +
                "   Onsdag den 12. februar 2014: 1 stk middag", DosisTilTekstWrapper.convertLongText(dosage));
        Assert.assertNull(DosisTilTekstWrapper.convertShortText(dosage));
        Assert.assertEquals(
                0.071428571,
                DosisTilTekstWrapper.calculateDailyDosis(dosage).getValue().doubleValue(),
                0.000000001);
        Assert.assertEquals(DosageType.Fixed, DosisTilTekstWrapper.getDosageType(dosage));
    }

}
