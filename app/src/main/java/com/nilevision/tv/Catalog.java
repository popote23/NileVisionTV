package com.nilevision.tv;

/** Organisation façon récepteur numérique. Modifiez les URL ici pour utiliser vos propres listes M3U. */
public class Catalog {
    public static final String B = "https://iptv-org.github.io/iptv/";
    public static class Cat {
        public final String title, url, filter; public final boolean sat;
        Cat(String t, String u, String f, boolean s) { title = t; url = u; filter = f; sat = s; }
    }
    public static final Cat[] CATS = {
        new Cat("🇲🇦  Chaînes marocaines",  B + "countries/ma.m3u", null, false),
        new Cat("🇩🇿  Chaînes algériennes", B + "countries/dz.m3u", null, false),
        new Cat("🇪🇬  Chaînes égyptiennes", B + "countries/eg.m3u", null, false),
        new Cat("🇫🇷  Chaînes françaises",  B + "countries/fr.m3u", null, false),
        new Cat("🇬🇧  Chaînes anglaises",   B + "countries/uk.m3u", null, false),
        new Cat("🇺🇸  Chaînes USA",         B + "countries/us.m3u", null, false),
        new Cat("🎬  Cinéma arabe / égyptien", B + "languages/ara.m3u", "movie", false),
        new Cat("🎬  Cinéma français",      B + "languages/fra.m3u", "movie", false),
        new Cat("🎬  Cinéma anglais",       B + "languages/eng.m3u", "movie", false),
        new Cat("🎥  Documentaires arabes", B + "languages/ara.m3u", "documentary", false),
        new Cat("🎥  Documentaires français", B + "languages/fra.m3u", "documentary", false),
        new Cat("🎥  Documentaires monde",  B + "categories/documentary.m3u", null, false),
        new Cat("🎵  Musique arabe",        B + "languages/ara.m3u", "music", false),
        new Cat("🎵  Musique française",    B + "languages/fra.m3u", "music", false),
        new Cat("🎵  Musique monde",        B + "categories/music.m3u", null, false),
        new Cat("📰  Actualités",           B + "categories/news.m3u", null, false),
        new Cat("⚽  Sport",                B + "categories/sports.m3u", null, false),
        new Cat("🧸  Enfants",              B + "categories/kids.m3u", null, false),
        new Cat("🕌  Religion",             B + "categories/religious.m3u", null, false),
        new Cat("📡  Satellites",           null, null, true),
    };

    /** {nom, position orbitale, description} - Nilesat en premier. */
    public static final String[][] SATS = {
        {"Nilesat 201 / 301", "7°W", "Satellite égyptien. Principal satellite des bouquets arabes (Égypte, Maghreb, Moyen-Orient)."},
        {"Eutelsat 7 West A", "7°W", "Co-localisé avec Nilesat. Bouquets arabes et maghrébins."},
        {"Eutelsat 5 West A", "5°W", "Chaînes maghrébines et francophones."},
        {"Hot Bird 13B/13C/13E", "13°E", "Grand choix de chaînes européennes et arabes (cinéma, musique, documentaires)."},
        {"Astra 19.2°E", "19.2°E", "Chaînes françaises, allemandes, anglaises."},
        {"Badr / Arabsat", "26°E", "Bouquets arabes : ART, Rotana, MBC, etc."},
        {"Astra 28.2°E", "28.2°E", "Chaînes britanniques."},
        {"Hispasat", "30°W", "Chaînes espagnoles et latino-américaines."},
        {"Türksat", "42°E", "Chaînes turques et moyen-orientales."},
        {"Intelsat 902", "62°E", "Chaînes d'Asie, Moyen-Orient, Afrique."},
    };
}
