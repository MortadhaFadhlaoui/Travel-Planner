//
//  DayPlan.swift
//  TravelPlanner
//
//  Created by ESPRIT on 14/12/17.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//

import Foundation

class DayPanModel: NSObject {
    
    //properties
    var idDay: Int?
    var idPack: Int?
    var date: Date?
    var title: String?
    var descriptionday: String?
    
    
    //empty constructor
    
    override init()
    {
        
    }
    
    //construct with @name, @address, @latitude, and @longitude parameters
    
   
    
    
    //prints object's current state
    
    override var description: String {
        return "idPack: \(String(describing: idPack)), date: \(String(describing: date)), title: \(String(describing: title)), description: \(String(describing: descriptionday)))"
        
    }
}

