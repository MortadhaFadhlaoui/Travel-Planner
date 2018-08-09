//
//  Reservation.swift
//  TravelPlanner
//
//  Created by ESPRIT on 11/01/2018.
//  Copyright © 2018 AhmedMohamed. All rights reserved.
//

import Foundation
class ReservationModel: NSObject {
    
    //properties
    var idReservation: Int?
    var etat: String?
    var date: Date?
    var Pack: PackModel?
    var User: UserModel?
    //empty constructor
    
    override init()
    {
        
    }
    
    
    init(idReservation: Int, etat: String,date: Date, Pack: PackModel,User: UserModel) {
        
        self.idReservation = idReservation
        self.etat = etat
        self.date = date
        self.Pack = Pack
        self.User = User
    }
    
    
    //prints object's current state
    
    override var description: String {
        return "idReservation: \(String(describing: idReservation)), etat: \(String(describing: etat)),date: \(String(describing: date)), Pack: \(String(describing: Pack)), User: \(String(describing: User)))"
        
    }
}
