package com.tatyanavolkova.catbreeds.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Breed implements Serializable {

    @SerializedName("adaptability")
    private int adaptability;

    @SerializedName("affection_level")
    private int affectionLevel;

    @SerializedName("alt_names")
    private String altNames;

    @SerializedName("cfa_url")
    private String cfaUrl;

    @SerializedName("child_friendly")
    private int childFriendly;

    @SerializedName("country_code")
    private String countryCode;

    @SerializedName("country_codes")
    private String countryCodes;

    @SerializedName("description")
    private String description;

    @SerializedName("dog_friendly")
    private int dogFriendly;

    @SerializedName("energy_level")
    private int energyLevel;

    @SerializedName("experimental")
    private int experimental;
    @SerializedName("grooming")
    private int grooming;

    @SerializedName("hairless")
    private int hairless;

    @SerializedName("health_issues")
    private int healthIssues;

    @SerializedName("hypoallergenic")
    private int hypoallergenic;

    @SerializedName("id")
    private String id;

    @SerializedName("indoor")
    private int indoor;

    @SerializedName("intelligence")
    private int intelligence;

    @SerializedName("lap")
    private int lap;

    @SerializedName("life_span")
    private String lifeSpan;

    @SerializedName("name")
    private String name;

    @SerializedName("natural")
    private int natural;

    @SerializedName("origin")
    private String origin;

    @SerializedName("rare")
    private int rare;

    @SerializedName("rex")
    private int rex;

    @SerializedName("shedding_level")
    private int sheddingLevel;

    @SerializedName("short_legs")
    private int shortLegs;
    @SerializedName("social_needs")
    private int socialNeeds;

    @SerializedName("stranger_friendly")
    private int strangerFriendly;

    @SerializedName("suppressed_tail")
    private int suppressedTail;

    @SerializedName("temperament")
    private String temperament;

    @SerializedName("vcahospitals_url")
    private String vcahospitalsUrl;

    @SerializedName("vetstreet_url")
    private String vetstreetUrl;

    @SerializedName("vocalisation")
    private int vocalisation;

    @SerializedName("weight")
    private Weight weight;

    @SerializedName("wikipedia_url")
    private String wikipediaUrl;

    @SerializedName("bidability")
    private int bidability;

    @SerializedName("cat_friendly")
    private int catFriendly;

    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getAdaptability() {
        return adaptability;
    }

    public void setAdaptability(int adaptability) {
        this.adaptability = adaptability;
    }

    public int getAffectionLevel() {
        return affectionLevel;
    }

    public void setAffectionLevel(int affectionLevel) {
        this.affectionLevel = affectionLevel;
    }

    public String getAltNames() {
        return altNames;
    }

    public void setAltNames(String altNames) {
        this.altNames = altNames;
    }

    public String getCfaUrl() {
        return cfaUrl;
    }

    public void setCfaUrl(String cfaUrl) {
        this.cfaUrl = cfaUrl;
    }

    public int getChildFriendly() {
        return childFriendly;
    }

    public void setChildFriendly(int childFriendly) {
        this.childFriendly = childFriendly;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCodes() {
        return countryCodes;
    }

    public void setCountryCodes(String countryCodes) {
        this.countryCodes = countryCodes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDogFriendly() {
        return dogFriendly;
    }

    public void setDogFriendly(int dogFriendly) {
        this.dogFriendly = dogFriendly;
    }

    public int getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(int energyLevel) {
        this.energyLevel = energyLevel;
    }

    public int getExperimental() {
        return experimental;
    }

    public void setExperimental(int experimental) {
        this.experimental = experimental;
    }

    public int getGrooming() {
        return grooming;
    }

    public void setGrooming(int grooming) {
        this.grooming = grooming;
    }

    public int getHairless() {
        return hairless;
    }

    public void setHairless(int hairless) {
        this.hairless = hairless;
    }

    public int getHealthIssues() {
        return healthIssues;
    }

    public void setHealthIssues(int healthIssues) {
        this.healthIssues = healthIssues;
    }

    public int getHypoallergenic() {
        return hypoallergenic;
    }

    public void setHypoallergenic(int hypoallergenic) {
        this.hypoallergenic = hypoallergenic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIndoor() {
        return indoor;
    }

    public void setIndoor(int indoor) {
        this.indoor = indoor;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getLap() {
        return lap;
    }

    public void setLap(int lap) {
        this.lap = lap;
    }

    public String getLifeSpan() {
        return lifeSpan;
    }

    public void setLifeSpan(String lifeSpan) {
        this.lifeSpan = lifeSpan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNatural() {
        return natural;
    }

    public void setNatural(int natural) {
        this.natural = natural;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public int getRare() {
        return rare;
    }

    public void setRare(int rare) {
        this.rare = rare;
    }

    public int getRex() {
        return rex;
    }

    public void setRex(int rex) {
        this.rex = rex;
    }

    public int getSheddingLevel() {
        return sheddingLevel;
    }

    public void setSheddingLevel(int sheddingLevel) {
        this.sheddingLevel = sheddingLevel;
    }

    public int getShortLegs() {
        return shortLegs;
    }

    public void setShortLegs(int shortLegs) {
        this.shortLegs = shortLegs;
    }

    public int getSocialNeeds() {
        return socialNeeds;
    }

    public void setSocialNeeds(int socialNeeds) {
        this.socialNeeds = socialNeeds;
    }

    public int getStrangerFriendly() {
        return strangerFriendly;
    }

    public void setStrangerFriendly(int strangerFriendly) {
        this.strangerFriendly = strangerFriendly;
    }

    public int getSuppressedTail() {
        return suppressedTail;
    }

    public void setSuppressedTail(int suppressedTail) {
        this.suppressedTail = suppressedTail;
    }

    public String getTemperament() {
        return temperament;
    }

    public void setTemperament(String temperament) {
        this.temperament = temperament;
    }

    public String getVcahospitalsUrl() {
        return vcahospitalsUrl;
    }

    public void setVcahospitalsUrl(String vcahospitalsUrl) {
        this.vcahospitalsUrl = vcahospitalsUrl;
    }

    public String getVetstreetUrl() {
        return vetstreetUrl;
    }

    public void setVetstreetUrl(String vetstreetUrl) {
        this.vetstreetUrl = vetstreetUrl;
    }

    public int getVocalisation() {
        return vocalisation;
    }

    public void setVocalisation(int vocalisation) {
        this.vocalisation = vocalisation;
    }

    public Weight getWeight() {
        return weight;
    }

    public void setWeight(Weight weight) {
        this.weight = weight;
    }

    public String getWikipediaUrl() {
        return wikipediaUrl;
    }

    public void setWikipediaUrl(String wikipediaUrl) {
        this.wikipediaUrl = wikipediaUrl;
    }

    public int getBidability() {
        return bidability;
    }

    public void setBidability(int bidability) {
        this.bidability = bidability;
    }

    public int getCatFriendly() {
        return catFriendly;
    }

    public void setCatFriendly(int catFriendly) {
        this.catFriendly = catFriendly;
    }
}
