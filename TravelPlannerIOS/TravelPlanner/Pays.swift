//
//  Pays.swift
//  TravelPlanner
//
//  Created by ESPRIT on 13/12/17.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//

import Foundation

class PaysModel: NSObject {
    
    //properties
    var idPays: Int?
    var nomPays: String?
    var image: String?
    var Pack: PackModel?
    
    //empty constructor
    
    override init()
    {
        
    }
    
    //construct with @name, @address, @latitude, and @longitude parameters
    
    init(idPays: Int, nomPays: String,image: String, Pack: PackModel) {
        
        self.idPays = idPays
        self.nomPays = nomPays
        self.image = image
        self.Pack = Pack
        
    }
    
    
    //prints object's current state
    
    override var description: String {
        return "idPays: \(String(describing: idPays)), nomPays: \(String(describing: nomPays)),image: \(String(describing: image)), Pack: \(String(describing: Pack)))"
        
    }
}


