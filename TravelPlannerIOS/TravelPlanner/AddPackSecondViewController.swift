//
//  AddPackSecondViewController.swift
//  TravelPlanner
//
//  Created by ESPRIT on 13/12/17.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//

import UIKit
import JKSteppedProgressBar
import CoreLocation
import MapKit
import GooglePlacesSearchController
class AddPackSecondViewController: UIViewController , UITableViewDelegate, UITableViewDataSource {
var arrivertxt: String = ""
    var feedItems : [String]  = []
     let GoogleSearchPlaceAPIKey  = "AIzaSyB9ndjdyIKmf0gwVDzQVpVeWgCFFEFxG5g"
    @IBOutlet weak var tableview: UITableView!
    @IBOutlet weak var destination: UIButton!
    @IBOutlet weak var prog: SteppedProgressBar!
    override func viewDidLoad() {
        super.viewDidLoad()
        print(AddPackThirdViewController.pack)
        
        prog.stepDrawingMode = .fill
        
        prog.titles = ["Step 1".localized, "Step 2".localized, "Step 3".localized,]
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */
    @IBAction func goback(_ sender: Any) {
        
        
        
        let controller = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "firstaddpack")
        self.show(controller, sender: true)
    }

    @IBAction func next(_ sender: Any) {
        if(feedItems.count != 0){
            AddPackThirdViewController.pays = feedItems
            let controller = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "thirdaddpack")
            self.show(controller, sender: true)
        }else{
            showToast(message: "Select at least 1 destination")
            
        }
        
    }
    
    @IBAction func selectdest(_ sender: Any) {
        let controller = GooglePlacesSearchController(
            apiKey: GoogleSearchPlaceAPIKey
        )
        controller.didSelectGooglePlace { (place) in
            self.arrivertxt = place.country
            print(place.country)
            let trimmedString = place.country.removingWhitespaces()
            self.feedItems.append(trimmedString)
            self.tableview.reloadData()
            controller.dismiss(animated: false, completion: {
                
            })
        }
        controller.isActive = false
        present(controller, animated: true,completion: nil)
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        return self.feedItems.count
        
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell:UITableViewCell = tableView.dequeueReusableCell(withIdentifier: "mydestinationcell", for: indexPath)
        let NameCountry: String!
        
        NameCountry = self.feedItems[indexPath.row]
        
        
        //cell.textLabel?.text = "Hello from new Cell"
        
        let lblCountry:UILabel = cell.viewWithTag(1) as! UILabel
        lblCountry.text = NameCountry
        
        return cell
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
extension String {
    func removingWhitespaces() -> String {
        return components(separatedBy: .whitespaces).joined()
    }
}

