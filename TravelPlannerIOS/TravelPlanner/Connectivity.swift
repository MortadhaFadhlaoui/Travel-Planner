//
//  Connectivity.swift
//  TravelPlanner
//
//  Created by ESPRIT on 11/01/2018.
//  Copyright © 2018 AhmedMohamed. All rights reserved.
//

import Foundation
import Alamofire
class Connectivity {
    class func isConnectedToInternet() ->Bool {
        return NetworkReachabilityManager()!.isReachable
    }
}

