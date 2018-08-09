//
//  AddPackViewController.swift
//  TravelPlanner
//
//  Created by ESPRIT on 12/12/2017.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//

import UIKit
import JKSteppedProgressBar
import CoreLocation
import MapKit
import GooglePlacesSearchController
import Alamofire
import D2PDatePicker
extension String {
    var localized: String {
        return NSLocalizedString(self, comment: "")
    }
}
class AddPackViewController: UIViewController ,SCPopDatePickerDelegate{
    let datePicker = SCPopDatePicker()
    let date = Date()
    lazy var dateFormatter: DateFormatter = {
        
        var formatter = DateFormatter()
        formatter.timeStyle = .none
        formatter.dateStyle = .medium
        return formatter
    }()
    
    @IBOutlet weak var price: UITextField!
    @IBOutlet weak var dateretour: UIButton!
    @IBOutlet weak var datedepart: UIButton!
    let GoogleSearchPlaceAPIKey  = "AIzaSyB9ndjdyIKmf0gwVDzQVpVeWgCFFEFxG5g"
    let daysToAdd = 2
    var dateComponent = DateComponents()
    var departtxt: String = ""
    var arrivertxt: String = ""
    var lastdepart: String = ""
    var x = 2
    var datedeparttxt: Date? = nil
    var datearrivetxt: Date? = nil
    var futureDate: Date? = nil
    @IBOutlet weak var depart: UIButton!
    @IBOutlet weak var prog: SteppedProgressBar!
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.endEditing(true)
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        prog.stepDrawingMode = .fill
    
prog.titles = ["Step 1".localized, "Step 2".localized, "Step 3".localized,]
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func selectdepart(_ sender: Any) {
        let controller = GooglePlacesSearchController(
            apiKey: GoogleSearchPlaceAPIKey
        )
        controller.didSelectGooglePlace { (place) in
            self.departtxt = place.country
            self.depart.setTitle(place.country, for: .normal)
            self.lastdepart = place.country
            self.depart.setTitleColor(.blue, for: .normal)
            controller.dismiss(animated: false, completion: {
                
            })
        }
        controller.isActive = false
        present(controller, animated: true,completion: nil)

    }

    @IBAction func next(_ sender: Any) {
        /*let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let newViewController = storyBoard.instantiateViewController(withIdentifier: "secondaddpack") as! UIViewController
        self.present(newViewController, animated: true, completion: nil)*/
       
        
        if(lastdepart != "" && price.text! != "" && datedeparttxt != nil && datearrivetxt != nil){
            let p = PackModel()
            let trimmedString = lastdepart.removingWhitespaces()
            
            p.nomDepart = trimmedString
            p.prix = Int(price.text!)
          
            p.dateDebut = datedeparttxt
            p.dateFin = datearrivetxt
            print(p)
        AddPackThirdViewController.pack = p
            
            let controller = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "secondaddpack")
            self.show(controller, sender: true)
        }else{
           showToast(message: "Please fill out all inputs")
        }
        
        
        
        

    }
    
    @IBAction func setdatedepart(_ sender: Any) {
        self.datePicker.tapToDismiss = true
        self.datePicker.datePickerType = SCDatePickerType.date
        self.datePicker.showBlur = true
        self.datePicker.datePickerStartDate = self.date
        self.datePicker.btnFontColour = UIColor.white
        self.datePicker.btnColour = UIColor.blue
        self.datePicker.showCornerRadius = false
        self.datePicker.delegate = self
        self.datePicker.show(attachToView: self.view)
        x = 1
    }
    func scPopDatePickerDidSelectDate(_ date: Date) {
        
        if x == 1 {
            datedeparttxt = date
            self.datedepart.setTitleColor(.blue, for: .normal)
            datedepart.setTitle(dateFormatter.string(from: datedeparttxt!) , for: .normal)
        }
      if x == 0 {
            datearrivetxt = date
        self.dateretour.setTitleColor(.blue, for: .normal)
            dateretour.setTitle(dateFormatter.string(from: datearrivetxt!) , for: .normal)
        }
        
    }
    @IBAction func dateretour(_ sender: Any) {
        if datedeparttxt != nil {
            
            
            
            dateComponent.day = daysToAdd
            
            futureDate = Calendar.current.date(byAdding: dateComponent, to: datedeparttxt!)
            
            
            self.datePicker.tapToDismiss = true
            self.datePicker.datePickerType = SCDatePickerType.date
            self.datePicker.showBlur = true
            self.datePicker.datePickerStartDate = self.futureDate!
            self.datePicker.btnFontColour = UIColor.white
            self.datePicker.btnColour = UIColor.blue
            self.datePicker.showCornerRadius = false
            self.datePicker.delegate = self
            self.datePicker.show(attachToView: self.view)
            x = 0
        }else{
            showToast(message: "Set up a start date")
        }
    }
    func showToast(message : String) {
        
        let toastLabel = UILabel(frame: CGRect(x: self.view.frame.size.width/2 - 95, y: self.view.frame.size.height-100, width: 170, height: 35))
        toastLabel.backgroundColor = UIColor.black.withAlphaComponent(0.6)
        toastLabel.textColor = UIColor.white
        toastLabel.textAlignment = .center;
        toastLabel.font = UIFont(name: "Montserrat-Light", size: 12.0)
        toastLabel.text = message
        toastLabel.alpha = 1.0
        toastLabel.layer.cornerRadius = 10;
        toastLabel.clipsToBounds  =  true
        self.view.addSubview(toastLabel)
        UIView.animate(withDuration: 4.0, delay: 0.1, options: .curveEaseOut, animations: {
            toastLabel.alpha = 0.0
        }, completion: {(isCompleted) in
            toastLabel.removeFromSuperview()
        })
    }

}
