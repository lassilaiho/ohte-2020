package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class KassapaateTest {

    Kassapaate paate;

    @Before
    public void setUp() {
        paate = new Kassapaate();
    }

    @Test
    public void rahamaaraAlussaOikein() {
        assertEquals(100000, paate.kassassaRahaa());
    }

    @Test
    public void edullisiaLounaitaAlussaOikein() {
        assertEquals(0, paate.edullisiaLounaitaMyyty());
    }

    @Test
    public void maukkaitaLounaitaAlussaOikein() {
        assertEquals(0, paate.maukkaitaLounaitaMyyty());
    }

    @Test
    public void syoEdullisestiKasvattaaRahamaaraaJosMaksuRiittaa() {
        paate.syoEdullisesti(250);
        assertEquals(100240, paate.kassassaRahaa());
    }

    @Test
    public void syoEdullisestiPalauttaaOikeanVaihtorahanJosMaksuRiittaa() {
        assertEquals(10, paate.syoEdullisesti(250));
    }

    @Test
    public void syoEdullisestiKasvattaaMyytyjaLounaitaJosMaksuRiittaa() {
        paate.syoEdullisesti(250);
        assertEquals(1, paate.edullisiaLounaitaMyyty());
    }

    @Test
    public void syoEdullisestiEiMuutaRahamaaraaJosMaksuEiRiita() {
        paate.syoEdullisesti(200);
        assertEquals(100000, paate.kassassaRahaa());
    }

    @Test
    public void syoEdullisestiPalauttaaKokoMaksunJosMaksuEiRiita() {
        assertEquals(200, paate.syoEdullisesti(200));
    }

    @Test
    public void syoEdullisestiEiMuutaMyytyjaLounaitaJosMaksuEiRiita() {
        paate.syoEdullisesti(200);
        assertEquals(0, paate.edullisiaLounaitaMyyty());
    }

    @Test
    public void syoMaukkaastiKasvattaaRahamaaraaJosMaksuRiittaa() {
        paate.syoMaukkaasti(420);
        assertEquals(100400, paate.kassassaRahaa());
    }

    @Test
    public void syoMaukkaastiPalauttaaOikeanVaihtorahanJosMaksuRiittaa() {
        assertEquals(20, paate.syoMaukkaasti(420));
    }

    @Test
    public void syoMaukkaastiKasvattaaMyytyjaLounaitaJosMaksuRiittaa() {
        paate.syoMaukkaasti(420);
        assertEquals(1, paate.maukkaitaLounaitaMyyty());
    }

    @Test
    public void syoMaukkaastiEiMuutaRahamaaraaJosMaksuEiRiita() {
        paate.syoMaukkaasti(399);
        assertEquals(100000, paate.kassassaRahaa());
    }

    @Test
    public void syoMaukkaastiPalauttaaKokoMaksunJosMaksuEiRiita() {
        assertEquals(399, paate.syoMaukkaasti(399));
    }

    @Test
    public void syoMaukkaastiEiMuutaMyytyjaLounaitaJosMaksuEiRiita() {
        paate.syoMaukkaasti(399);
        assertEquals(0, paate.maukkaitaLounaitaMyyty());
    }

    @Test
    public void syoEdullisestiVeloittaaOikeinJosSaldoRiittaa() {
        Maksukortti kortti = new Maksukortti(250);
        paate.syoEdullisesti(kortti);
        assertEquals(10, kortti.saldo());
    }

    @Test
    public void syoEdullisestiKasvattaaMyytyjaLounaitaJosSaldoRiittaa() {
        Maksukortti kortti = new Maksukortti(250);
        paate.syoEdullisesti(kortti);
        assertEquals(1, paate.edullisiaLounaitaMyyty());
    }

    @Test
    public void syoEdullisestiPalauttaaTrueJosSaldoRiittaa() {
        Maksukortti kortti = new Maksukortti(250);
        assertEquals(true, paate.syoEdullisesti(kortti));
    }

    @Test
    public void syoEdullisestiEiMuutaSaldoaJosSaldoEiRiita() {
        Maksukortti kortti = new Maksukortti(200);
        paate.syoEdullisesti(kortti);
        assertEquals(200, kortti.saldo());
    }

    @Test
    public void syoEdullisestiPalauttaaFalseJosSaldoEiRiita() {
        Maksukortti kortti = new Maksukortti(200);
        assertEquals(false, paate.syoEdullisesti(kortti));
    }

    @Test
    public void syoEdullisestiEiMuutaMyytyjaLounaitaJosSaldoEiRiita() {
        Maksukortti kortti = new Maksukortti(200);
        paate.syoEdullisesti(kortti);
        assertEquals(0, paate.edullisiaLounaitaMyyty());
    }

    @Test
    public void syoEdullisestiEiMuutaKassanRahamaaraa() {
        Maksukortti kortti = new Maksukortti(250);
        paate.syoEdullisesti(kortti);
        assertEquals(100000, paate.kassassaRahaa());
    }

    @Test
    public void syoMaukkaastiVeloittaaOikeinJosSaldoRiittaa() {
        Maksukortti kortti = new Maksukortti(420);
        paate.syoMaukkaasti(kortti);
        assertEquals(20, kortti.saldo());
    }

    @Test
    public void syoMaukkaastiKasvattaaMyytyjaLounaitaJosSaldoRiittaa() {
        Maksukortti kortti = new Maksukortti(420);
        paate.syoMaukkaasti(kortti);
        assertEquals(1, paate.maukkaitaLounaitaMyyty());
    }

    @Test
    public void syoMaukkaastiPalauttaaTrueJosSaldoRiittaa() {
        Maksukortti kortti = new Maksukortti(420);
        assertEquals(true, paate.syoMaukkaasti(kortti));
    }

    @Test
    public void syoMaukkaastiEiMuutaSaldoaJosSaldoEiRiita() {
        Maksukortti kortti = new Maksukortti(399);
        paate.syoMaukkaasti(kortti);
        assertEquals(399, kortti.saldo());
    }

    @Test
    public void syoMaukkaastiPalauttaaFalseJosSaldoEiRiita() {
        Maksukortti kortti = new Maksukortti(399);
        assertEquals(false, paate.syoMaukkaasti(kortti));
    }

    @Test
    public void syoMaukkaastiEiMuutaMyytyjaLounaitaJosSaldoEiRiita() {
        Maksukortti kortti = new Maksukortti(399);
        paate.syoMaukkaasti(kortti);
        assertEquals(0, paate.edullisiaLounaitaMyyty());
    }

    @Test
    public void syoMaukkaastiEiMuutaKassanRahamaaraa() {
        Maksukortti kortti = new Maksukortti(420);
        paate.syoMaukkaasti(kortti);
        assertEquals(100000, paate.kassassaRahaa());
    }

    @Test
    public void kortinLatausPositiivisellaRahamaarallaMuuttaaSaldoa() {
        Maksukortti kortti = new Maksukortti(50);
        paate.lataaRahaaKortille(kortti, 100);
        assertEquals(150, kortti.saldo());
    }

    @Test
    public void kortinLatausPositiivisellaRahamaarallaLisaaKassaanRahaa() {
        Maksukortti kortti = new Maksukortti(60);
        paate.lataaRahaaKortille(kortti, 140);
        assertEquals(100140, paate.kassassaRahaa());
    }

    @Test
    public void kortinLatausNegatiivisellaRahamaarallaEiMuutaSaldoa() {
        Maksukortti kortti = new Maksukortti(50);
        paate.lataaRahaaKortille(kortti, -20);
        assertEquals(50, kortti.saldo());
    }

    @Test
    public void kortinLatausNegatiivisellaRahamaarallaEiMuutaKassanRahamaaraa() {
        Maksukortti kortti = new Maksukortti(30);
        paate.lataaRahaaKortille(kortti, -20);
        assertEquals(100000, paate.kassassaRahaa());
    }
}
