package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti != null);
    }

    @Test
    public void saldoAlussaOikein() {
        assertEquals(10, kortti.saldo());
    }

    @Test
    public void latausKasvattaaSaldoa() {
        kortti.lataaRahaa(15);
        assertEquals(25, kortti.saldo());
    }

    @Test
    public void ottoVahentaaSaldoaJosRahaaRiittavasti() {
        kortti.otaRahaa(5);
        assertEquals(5, kortti.saldo());
    }

    @Test
    public void ottoEiMuutaSaldoaJosRahaaLiianVahan() {
        kortti.otaRahaa(13);
        assertEquals(10, kortti.saldo());
    }

    @Test
    public void ottoPalauttaaTrueJosRahaaRiittaa() {
        assertEquals(true, kortti.otaRahaa(5));
    }

    @Test
    public void ottoPalauttaaFalseJosRahaEiRiita() {
        assertEquals(false, kortti.otaRahaa(13));
    }

    @Test
    public void toStringToimii() {
        assertEquals("saldo: 0.10", kortti.toString());
    }
}
