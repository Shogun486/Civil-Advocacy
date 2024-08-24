package com.example.civiladvocacyapplication;

import java.io.Serializable;

public class Politician implements Serializable
{
    private String name, office, party, image, address, website,
            email, phone, facebook, youtube, twitter;



    // All attributes of the politician that can be gathered, if available
    public Politician(String name, String office, String party, String image,
                      String address, String website, String email, String phone,
                      String facebook, String youtube, String twitter)
    {
        this.name = name;
        this.office = office;
        this.party = party;
        this.image = image;
        this.address = address;
        this.website = website;
        this.email = email;
        this.phone = phone;
        this.facebook = facebook;
        this.youtube = youtube;
        this.twitter = twitter;
    }



    public String getName() {
        return name;
    }

    public String getOffice() {
        return office;
    }

    public String getParty() {
        return party;
    }

    public String getImage() {
        return image;
    }

    public String getAddress() {
        return address;
    }

    public String getWebsite() {
        return website;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getYoutube() {
        return youtube;
    }

    public String getTwitter() {
        return twitter;
    }
}
