//
//  Pack.swift
//  TravelPlanner
//
//  Created by ESPRIT on 13/12/17.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//

import Foundation


class PackModel: NSObject {
    
    //properties
    var idPack: Int?
    var dateDebut: Date?
    var dateFin: Date?
    var nomDepart: String?
    var prix: Int?
    
    
    //empty constructor
    
    override init()
    {
        
    }
    
    //construct with @name, @address, @latitude, and @longitude parameters
    
    init(idPack: Int, dateDebut: Date, dateFin: Date, nomDepart: String,prix: Int) {
        
        self.idPack = idPack
        self.dateDebut = dateDebut
        self.dateFin = dateFin
        self.nomDepart = nomDepart
        self.prix = prix
        
    }
    
    
    //prints object's current state
    
    override var description: String {
        return "idPack: \(String(describing: idPack)), dateDebut: \(String(describing: dateDebut)), dateFin: \(String(describing: dateFin)), nomDepart: \(String(describing: nomDepart)), prix: \(String(describing: prix)))"
        
}
}

