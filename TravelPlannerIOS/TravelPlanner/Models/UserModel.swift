//
//  UserModel.swift
//  TravelPlanner
//
//  Created by Ahmed Mohamed on 15/11/2017.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//

import UIKit

class UserModel: NSObject {

    //properties
    var idUser: Int?
    var nom: String?
    var prenom: String?
    var username: String?
    var password: String?
    var image: String?
    var email: String?
    var numtel: String?
    var role: String?
    
    
    //empty constructor
    
    override init()
    {
        
    }
    
    //construct with @name, @address, @latitude, and @longitude parameters
    
    init(idd: Int, nom: String, prenom: String, username: String,password: String,image: String,email: String,numtel: String,role: String) {
        
        self.nom = nom
        self.idUser = idd
        self.prenom = prenom
        self.username = username
        self.password = password
        self.image = image
        self.email = email
        self.numtel = numtel
        self.role = role
        
    }
    
    
    //prints object's current state
    
    override var description: String {
        return "Name: \(String(describing: nom)), prenom: \(String(describing: prenom)), username: \(String(describing: username)), password: \(String(describing: password)), id: \(String(describing: idUser)), id: \(String(describing: role))"
        
    }
    
    
}
