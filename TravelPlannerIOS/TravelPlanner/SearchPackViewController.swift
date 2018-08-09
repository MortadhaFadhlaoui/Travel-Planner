//
//  SearchPackViewController.swift
//  TravelPlanner
//
//  Created by ESPRIT on 09/12/2017.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//

import UIKit
import D2PDatePicker
import CoreLocation
import MapKit
import GooglePlacesSearchController
import Alamofire
class SearchPackViewController: UIViewController, SCPopDatePickerDelegate, UITableViewDelegate, UITableViewDataSource {
    let GoogleSearchPlaceAPIKey  = "AIzaSyB9ndjdyIKmf0gwVDzQVpVeWgCFFEFxG5g"
    let datePicker = SCPopDatePicker()
    let date = Date()
    var feedItems : [String]  = []
     static var paysItems : [PaysModel]  = []
    static var paksItems : [PackModel]  = []
    let daysToAdd = 2
    var dateComponent = DateComponents()
    @IBOutlet weak var arriverdate: UIButton!
    @IBOutlet weak var departdate: UIButton!
    @IBOutlet weak var arriver: UIButton!
    @IBOutlet weak var image: UIImageView!
    @IBOutlet weak var tableView: UITableView!
    
    let borderAlpha : CGFloat = 0.7
    let cornerRadius : CGFloat = 5.0
    
    @IBOutlet weak var departt: UIButton!
    lazy var dateFormatter: DateFormatter = {
        
        var formatter = DateFormatter()
        formatter.timeStyle = .none
        formatter.dateStyle = .medium
        return formatter
    }()
    
    var departtxt: String = ""
    var arrivertxt: String = ""
    var x = 2
    var datedeparttxt: Date? = nil
    var datearrivetxt: Date? = nil
    var futureDate: Date? = nil
    override func viewDidLoad() {
        super.viewDidLoad()
     
     
        let tapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(imageTapped(tapGestureRecognizer:)))
        image.isUserInteractionEnabled = true
        image.addGestureRecognizer(tapGestureRecognizer)
        
       
        arriverdate.backgroundColor = UIColor.clear
        arriverdate.layer.borderWidth = 1.0
        arriverdate.layer.borderColor = UIColor(white: 1.0, alpha: borderAlpha).cgColor
        arriverdate.layer.cornerRadius = cornerRadius

        
        departdate.backgroundColor = UIColor.clear
        departdate.layer.borderWidth = 1.0
        departdate.layer.borderColor = UIColor(white: 1.0, alpha: borderAlpha).cgColor
        departdate.layer.cornerRadius = cornerRadius
      /*   let blurEffect = UIBlurEffect(style: UIBlurEffectStyle.light)
        let blurView = UIVisualEffectView(effect: blurEffect)
      
       let backgroundImage = UIImageView(frame: UIScreen.main.bounds)
        backgroundImage.image = UIImage(named: "backsearch")
        backgroundImage.contentMode =  UIViewContentMode.scaleAspectFill
        blurView.frame = backgroundImage.bounds
        backgroundImage.addSubview(blurView)
        self.view.insertSubview(backgroundImage, at: 0)
        self.tableView.backgroundView = backgroundImage*/
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
 
    @IBAction func arriverdatex(_ sender: Any) {
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
    @IBAction func departdatex(_ sender: Any) {
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
   
    func imageTapped(tapGestureRecognizer: UITapGestureRecognizer)
    {
        if ((departtxt).isEmpty) {
            showToast(message: "Set up start place")
        }else if((feedItems).isEmpty){
            showToast(message: "Set up arrive place")
        }else if(datedeparttxt == nil){
            showToast(message: "Set up start date")
        }else if(datearrivetxt == nil){
            showToast(message: "Set up arrive date")
        }else{
            if Connectivity.isConnectedToInternet() {
            var y:String = ""
            for item in feedItems {
                y += item+";"
            }
            
            let x1 = y.substring(to:y.index(y.endIndex, offsetBy: -1))
            let formatter = DateFormatter()
            formatter.dateFormat = "yyyy-MM-dd"
            formatter.string(from: datedeparttxt!)
            let x2 = formatter.string(from: datedeparttxt!).substring(to:dateFormatter.string(from: datedeparttxt!).index(dateFormatter.string(from: datedeparttxt!).startIndex, offsetBy: 10))
            let x3 = formatter.string(from: datedeparttxt!).substring(to:dateFormatter.string(from: datearrivetxt!).index(dateFormatter.string(from: datearrivetxt!).startIndex, offsetBy: 10))
            let urlPath = URL(string: NavigatorData.URL+"/api/recherche")
            
            let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec ]
            
            let parameters = [
                "nomdepart" : departtxt,
                "datedepart" : x2,
                "datearrive" : x3,
                "nomarrive" : x1] as [String : Any]
            
            Alamofire.request(urlPath!, method: .post,parameters: parameters,encoding: JSONEncoding.default,headers: Auth_header ).responseJSON { responsep in
                if(responsep.result.isSuccess){
                    let dict = responsep.result.value as? Dictionary<String, AnyObject>
                    let success = dict?["success"]! as! NSNumber
                    
                    if(success==1){
                    let packs = dict?["packs"] as? NSArray
                        SearchPackViewController.paysItems.removeAll()
                        SearchPackViewController.paysItems.removeAll()
                        for items in (packs! as NSArray) {
                            let item = items as? Dictionary<String, AnyObject>
                            let pack = item?["pack"] as? Dictionary<String, AnyObject>
                            let idPack = pack!["id"] as! Int
                            let date_debut = pack!["date_debut"] as! String
                            let date_fin = pack!["date_fin"] as! String
                            let date_d = date_debut.substring(to:date_debut.index(date_debut.startIndex, offsetBy: 10))
                            let date_f = date_fin.substring(to:date_fin.index(date_fin.startIndex, offsetBy: 10))
                            let dateFormatter = DateFormatter()
                            dateFormatter.dateFormat = "yyyy-MM-dd" //Your date format
                            dateFormatter.timeZone = TimeZone(abbreviation: "GMT+0:00") //Current time zone
                            
                            let date_db = dateFormatter.date(from: date_d) //according to date
                            let date_fi = dateFormatter.date(from: date_f) //according to date
                            let nom_depart = pack!["nom_depart"] as! String
                            let prix = pack!["prix"] as! Int
                            
                            let packm = PackModel()
                            packm.idPack = idPack as Int?
                            packm.dateDebut = date_db
                            packm.dateFin = date_fi
                            packm.nomDepart = nom_depart
                            packm.prix = prix
                            
                            let pays = item?["pays"] as? Dictionary<String, AnyObject>
                            let idPays = pays!["id"] as! Int
                            let nom_pays = pays!["nom_pays"] as! String
                            let paysm = PaysModel()
                            paysm.idPays = idPays as Int?
                            paysm.nomPays = nom_pays
                            paysm.Pack = packm
                            
                            SearchPackViewController.paysItems.append(paysm)
                            SearchPackViewController.paksItems.append(packm)
                            
                            
                        }
                        let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
                        let newViewController = storyBoard.instantiateViewController(withIdentifier: "affichese") as! AfficheSearchedPackViewController
                        self.show(newViewController, sender: true)
                    }else{
                        self.showToast(message: "No pack Found")
                    }
                    
                }else if(responsep.result.isFailure){
                    self.showToast(message: "Something wrong")
                }
                
            }
            }else{
                
                SweetAlert().showAlert("No internet connection", subTitle: "", style: AlertStyle.warning, buttonTitle:"Cancel", buttonColor:self.UIColorFromRGB(rgbValue: 0xD0D0D0) , otherButtonTitle:  "Open network!", otherButtonColor: self.UIColorFromRGB(rgbValue: 0xDD6B55)) { (isOtherButton) -> Void in
                    if isOtherButton == true {
                        SweetAlert().showAlert("Quit", subTitle: "Sure you want to quit!", style: AlertStyle.error, buttonTitle:"Cancel", buttonColor:self.UIColorFromRGB(rgbValue: 0xD0D0D0) , otherButtonTitle:  "Ok", otherButtonColor: self.UIColorFromRGB(rgbValue: 0xDD6B55)) { (isOtherButton) -> Void in
                            if isOtherButton == true {
                                
                            }
                            else {
                                
                                let storyBoard : UIStoryboard = UIStoryboard(name: "Main", bundle:nil)
                                
                                let login = storyBoard.instantiateViewController(withIdentifier: "login") as! ViewController
                                
                                self.present(login, animated: true, completion: nil)
                            }
                        }
                    }
                    else {
                        let url = URL(string: "App-Prefs:root=WIFI") //for WIFI setting app
                        let app = UIApplication.shared
                        app.openURL(url!)
                        
                    }
                }
                
            }
            
        
        }
        
    }
    func hexStringToUIColor (hex:String) -> UIColor {
        var cString:String = hex.trimmingCharacters(in: .whitespacesAndNewlines).uppercased()
        
        if (cString.hasPrefix("#")) {
            cString.remove(at: cString.startIndex)
        }
        
        if ((cString.characters.count) != 6) {
            return UIColor.gray
        }
        
        var rgbValue:UInt32 = 0
        Scanner(string: cString).scanHexInt32(&rgbValue)
        
        return UIColor(
            red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
            green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
            blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
            alpha: CGFloat(1.0)
        )
    }
    @IBAction func arriveaction(_ sender: Any) {
        let controller = GooglePlacesSearchController(
            apiKey: GoogleSearchPlaceAPIKey
        )
        controller.didSelectGooglePlace { (place) in
            self.arrivertxt = place.country
            if(self.feedItems.contains(place.country)){
                self.showToast(message: "You have already chose \(place.country)")
            }else{
                self.feedItems.append(place.country)
                self.tableView.reloadData()
            }
         
            controller.dismiss(animated: false, completion: {
                
            })
        }
        controller.isActive = false
        present(controller, animated: true,completion: nil)
        
    }
   
    @IBAction func depart(_ sender: Any) {
        let controller = GooglePlacesSearchController(
            apiKey: GoogleSearchPlaceAPIKey
        )
        controller.didSelectGooglePlace { (place) in
            self.departt.titleLabel?.minimumScaleFactor = 0.5
            self.departt.titleLabel?.adjustsFontSizeToFitWidth = true
            self.departtxt = place.country
            self.departt.setTitle(place.country, for: .normal)
            self.departt.setTitleColor(.white, for: .normal)
            controller.dismiss(animated: false, completion: {
                
            })
        }
        controller.isActive = false
        present(controller, animated: true,completion: nil)
    }
    
    func scPopDatePickerDidSelectDate(_ date: Date) {
        
        if x == 1 {
            datedeparttxt = date
            departdate.setTitle(dateFormatter.string(from: datedeparttxt!) , for: .normal)
        }
        if x == 0 {
            datearrivetxt = date
             arriverdate.setTitle(dateFormatter.string(from: datearrivetxt!) , for: .normal)
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
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
     
        return self.feedItems.count
        
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell:UITableViewCell = tableView.dequeueReusableCell(withIdentifier: "cell", for: indexPath)
        let NameCountry: String!
     
            NameCountry = self.feedItems[indexPath.row]
        
        
        //cell.textLabel?.text = "Hello from new Cell"
        
        let lblCountry:UILabel = cell.viewWithTag(1) as! UILabel
        lblCountry.text = NameCountry
        
        return cell
    }
    // Override to support editing the table view.
    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            // Delete the row from the data source
            feedItems.remove(at: indexPath.row)
            tableView.deleteRows(at: [indexPath], with: .fade)
        } else if editingStyle == .insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }
    }
    
    
    func UIColorFromRGB(rgbValue: UInt) -> UIColor {
        return UIColor(
            red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
            green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
            blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
            alpha: CGFloat(1.0)
        )
    }
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.endEditing(true)
    }
    
}


