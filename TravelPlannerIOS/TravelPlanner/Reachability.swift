//
//  Reachability.swift
//  TravelPlanner
//
//  Created by ESPRIT on 11/01/2018.
//  Copyright © 2018 AhmedMohamed. All rights reserved.
//

import Foundation
import CoreLocation

open class Reachability {
    class func isLocationServiceEnabled() -> Bool {
        if CLLocationManager.locationServicesEnabled() {
            switch(CLLocationManager.authorizationStatus()) {
            case .notDetermined, .restricted, .denied:
                return false
            case .authorizedAlways, .authorizedWhenInUse:
                return true
            }
        } else {
            print("Location services are not enabled")
            return false
        }
    }
}
