//
//  NearbyModel.swift
//  TravelPlanner
//
//  Created by ESPRIT on 22/11/2017.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//

import UIKit

class NearbyModel: NSObject {
    
    //properties
    var id: String?
    var name: String?
    var categorie: String?
    var tips: String?
    var image: String?
    var iconcat: String?
    var lat: Double?
    var lng: Double?
    var distance: Int?
    var rating: Double?
    var ratingColor: String?
    var isOpen: Bool?
    
 
    
    //empty constructor
    
    override init()
    {
        
    }
    
    //construct with @name, @address, @latitude, and @longitude parameters
    
    init(name: String, categorie: String, tips: String,image: String,iconcat: String,lat: Double,lng: Double,distance: Int,rating: Double,ratingColor: String,isOpen: Bool) {
        

        self.name = name
        self.categorie = categorie
        self.tips = tips
        self.image = image
        self.iconcat = iconcat
        self.lat = lat
        self.lng = lng
        self.distance = distance
        self.rating = rating
        self.ratingColor = ratingColor
        self.isOpen = isOpen
     
        
    }
    
    
    //prints object's current state
    
    override var description: String {
        return "Name: \(String(describing: name)), categorie: \(String(describing: categorie)), tips: \(String(describing: tips)), image: \(String(describing: image)),iconCategorie: \(String(describing: iconcat)), lat: \(String(describing: lat)), lng: \(String(describing: lng)), distance: \(String(describing: distance)), rating: \(String(describing: rating)), ratingColor: \(String(describing: ratingColor)), isOpen: \(String(describing: isOpen))"
        
}
}
