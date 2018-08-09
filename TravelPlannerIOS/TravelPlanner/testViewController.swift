//
//  testViewController.swift
//  TravelPlanner
//
//  Created by Ahmed Mohamed on 29/11/2017.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//

import UIKit
import GooglePlacePicker
import GooglePlaces
import GoogleMaps
class testViewController: GMSAutocompleteViewController {

    // TODO: Add a button to Main.storyboard to invoke onLaunchClicked.
    
    override func viewDidLoad() {
        let acController = GMSAutocompleteViewController()
        acController.delegate = self as! GMSAutocompleteViewControllerDelegate
        present(acController, animated: true, completion: nil)
    }
   
    func viewController(_ viewController: GMSAutocompleteViewController, didAutocompleteWith place: GMSPlace) {
        print("Place name: \(place.name)")
        print("Place address: \(place.formattedAddress)")
        print("Place attributions: \(place.attributions)")
        dismiss(animated: true, completion: nil)
    }
    
    func viewController(_ viewController: GMSAutocompleteViewController, didFailAutocompleteWithError error: Error) {
        // TODO: handle the error.
        print("Error: \(error)")
        dismiss(animated: true, completion: nil)
    }
    
    // User cancelled the operation.
    func wasCancelled(_ viewController: GMSAutocompleteViewController) {
        print("Autocomplete was cancelled.")
        dismiss(animated: true, completion: nil)
    }

}

