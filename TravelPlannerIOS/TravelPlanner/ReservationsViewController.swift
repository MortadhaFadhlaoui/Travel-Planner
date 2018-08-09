//
//  ReservationsViewController.swift
//  TravelPlanner
//
//  Created by ESPRIT on 11/01/2018.
//  Copyright © 2018 AhmedMohamed. All rights reserved.
//

import UIKit
import Alamofire
import CRRefresh
class ReservationsViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    var reservation = ReservationModel()
    var reservations : [ReservationModel]  = []
    var packs : [PackModel]  = []
    
    
    @IBOutlet weak var load: UIActivityIndicatorView!
    @IBOutlet weak var tableView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        load.isHidden = false
        self.load.startAnimating()
        if Connectivity.isConnectedToInternet() {
            tableView.isHidden = true
            let urlPath = URL(string: NavigatorData.URL+"/api/getreservation")
            let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec ]
            //let parameters = [:] as [String : Any]
            
            Alamofire.request(urlPath!, method: .post,encoding: JSONEncoding.default,headers: Auth_header ).responseJSON { responsep in
                if(responsep.result.isSuccess){
                    let dict = responsep.result.value as? Dictionary<String, AnyObject>
                    let success = dict?["success"]! as! NSNumber
                    
                    if(success==1){
                        let reservationsarray = dict?["reservations"] as? NSArray
                        let packsarray = dict?["packs"] as? NSArray
                        self.reservations = []
                        self.packs = []
                        for items in (packsarray! as NSArray) {
                            let itemm = items as? NSArray
                            let item = itemm?[0] as? Dictionary<String, AnyObject>
                            
                            let prix = item!["prix"] as! Int
                            let nom_depart = item!["nom_depart"] as! String
                            let date1 = item!["date_debut"] as! String
                            let date2 = item!["date_fin"] as! String
                            
                            
                            
                            let date_d1 = date1.substring(to:date1.index(date1.startIndex, offsetBy: 10))
                            let dateFormatter = DateFormatter()
                            dateFormatter.dateFormat = "yyyy-MM-dd" //Your date format
                            dateFormatter.timeZone = TimeZone(abbreviation: "GMT+0:00") //Current time zone
                            
                            
                            let date_db1 = dateFormatter.date(from: date_d1) //according to date
                            
                            
                            let date_d2 = date2.substring(to:date2.index(date2.startIndex, offsetBy: 10))
                            
                            let date_db2 = dateFormatter.date(from: date_d2) //according to date
                            
                            let packobject = PackModel()
                            packobject.nomDepart = nom_depart
                            packobject.prix = prix
                            packobject.dateDebut = date_db1
                            packobject.dateFin = date_db2
                            self.packs.append(packobject)
                            self.tableView.reloadData()
                            
                        }
                        //self.dayplansitem.removeAll()
                        for items in (reservationsarray! as NSArray) {
                            let item = items as? Dictionary<String, AnyObject>
                            let idReservation = item!["id"] as! Int
                            let idPack = item!["pack_id"] as! Int
                            let idUser = item!["user_id"] as! Int
                            let etat = item!["etat"] as! String
                            let date = item!["date"] as! String
                            
                            
                            
                            let date_d = date.substring(to:date.index(date.startIndex, offsetBy: 10))
                            let dateFormatter = DateFormatter()
                            dateFormatter.dateFormat = "yyyy-MM-dd" //Your date format
                            dateFormatter.timeZone = TimeZone(abbreviation: "GMT+0:00") //Current time zone
                            
                            let date_db = dateFormatter.date(from: date_d) //according to date
                            
                            
                            
                            let reservationobject = ReservationModel()
                            reservationobject.idReservation = idReservation as Int?
                            reservationobject.Pack?.idPack = idPack as Int?
                            reservationobject.User?.idUser = idUser as Int?
                            reservationobject.etat = etat
                            reservationobject.date = date_db
                            
                            
                            self.reservations.append(reservationobject)
                            self.load.isHidden = true
                            self.tableView.isHidden = false
                            self.tableView.reloadData()
                            
                        }
                    }
                    else{
                        self.showToast(message: "No Reservation")
                    }
                }else if(responsep.result.isFailure){
                    print("le le")
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
        // Do any additional setup after loading the view.
    }
    override func viewWillAppear(_ animated: Bool) {
        tableView.cr.addHeadRefresh(animator: FastAnimator()) { [weak self] in
            /// start refresh
            /// Do anything you want...
            
            self?.load.isHidden = false
            self?.load.startAnimating()
            if Connectivity.isConnectedToInternet() {
                self?.tableView.isHidden = true
                let urlPath = URL(string: NavigatorData.URL+"/api/getreservation")
                let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec ]
                //let parameters = [:] as [String : Any]
                
                Alamofire.request(urlPath!, method: .post,encoding: JSONEncoding.default,headers: Auth_header ).responseJSON { responsep in
                    if(responsep.result.isSuccess){
                        let dict = responsep.result.value as? Dictionary<String, AnyObject>
                        let success = dict?["success"]! as! NSNumber
                        
                        if(success==1){
                            let reservationsarray = dict?["reservations"] as? NSArray
                            let packsarray = dict?["packs"] as? NSArray
                            self?.reservations = []
                            self?.packs = []
                            for items in (packsarray! as NSArray) {
                                let itemm = items as? NSArray
                                let item = itemm?[0] as? Dictionary<String, AnyObject>
                                
                                let prix = item!["prix"] as! Int
                                let nom_depart = item!["nom_depart"] as! String
                                let date1 = item!["date_debut"] as! String
                                let date2 = item!["date_fin"] as! String
                                
                                
                                
                                let date_d1 = date1.substring(to:date1.index(date1.startIndex, offsetBy: 10))
                                let dateFormatter = DateFormatter()
                                dateFormatter.dateFormat = "yyyy-MM-dd" //Your date format
                                dateFormatter.timeZone = TimeZone(abbreviation: "GMT+0:00") //Current time zone
                                
                                
                                let date_db1 = dateFormatter.date(from: date_d1) //according to date
                                
                                
                                let date_d2 = date2.substring(to:date2.index(date2.startIndex, offsetBy: 10))
                                
                                let date_db2 = dateFormatter.date(from: date_d2) //according to date
                                
                                let packobject = PackModel()
                                packobject.nomDepart = nom_depart
                                packobject.prix = prix
                                packobject.dateDebut = date_db1
                                packobject.dateFin = date_db2
                                self?.packs.append(packobject)
                                self?.tableView.reloadData()
                                
                            }
                            //self.dayplansitem.removeAll()
                            for items in (reservationsarray! as NSArray) {
                                let item = items as? Dictionary<String, AnyObject>
                                let idReservation = item!["id"] as! Int
                                let idPack = item!["pack_id"] as! Int
                                let idUser = item!["user_id"] as! Int
                                let etat = item!["etat"] as! String
                                let date = item!["date"] as! String
                                
                                
                                
                                let date_d = date.substring(to:date.index(date.startIndex, offsetBy: 10))
                                let dateFormatter = DateFormatter()
                                dateFormatter.dateFormat = "yyyy-MM-dd" //Your date format
                                dateFormatter.timeZone = TimeZone(abbreviation: "GMT+0:00") //Current time zone
                                
                                let date_db = dateFormatter.date(from: date_d) //according to date
                                
                                
                                
                                let reservationobject = ReservationModel()
                                reservationobject.idReservation = idReservation as Int?
                                reservationobject.Pack?.idPack = idPack as Int?
                                reservationobject.User?.idUser = idUser as Int?
                                reservationobject.etat = etat
                                reservationobject.date = date_db
                                
                                
                                self?.reservations.append(reservationobject)
                                self?.load.isHidden = true
                                self?.tableView.isHidden = false
                                self?.tableView.reloadData()
                                
                            }
                            self?.tableView.cr.endHeaderRefresh()
                        }
                        else{
                            self?.showToast(message: "No Reservation")
                        }
                    }else if(responsep.result.isFailure){
                        print("le le")
                    }
                    
                }
            }else{
                
                SweetAlert().showAlert("No internet connection", subTitle: "", style: AlertStyle.warning, buttonTitle:"Cancel", buttonColor:(self?.UIColorFromRGB(rgbValue: 0xD0D0D0))! , otherButtonTitle:  "Open network!", otherButtonColor: self?.UIColorFromRGB(rgbValue: 0xDD6B55)) { (isOtherButton) -> Void in
                    if isOtherButton == true {
                        SweetAlert().showAlert("Quit", subTitle: "Sure you want to quit!", style: AlertStyle.error, buttonTitle:"Cancel", buttonColor:((self?.UIColorFromRGB(rgbValue: 0xD0D0D0)))! , otherButtonTitle:  "Ok", otherButtonColor: self?.UIColorFromRGB(rgbValue: 0xDD6B55)) { (isOtherButton) -> Void in
                            if isOtherButton == true {
                                
                            }
                            else {
                                
                                let storyBoard : UIStoryboard = UIStoryboard(name: "Main", bundle:nil)
                                
                                let login = storyBoard.instantiateViewController(withIdentifier: "login") as! ViewController
                                
                                self?.present(login, animated: true, completion: nil)
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
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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
        var numOfSections: Int = 0
        if packs.count != 0
        {
            self.tableView.separatorStyle = .singleLine
            numOfSections            = 1
            self.tableView.backgroundView = nil
        }
        else
        {
            tableView.isHidden = false
            load.isHidden = true
            let noDataLabel: UILabel     = UILabel(frame: CGRect(x: 0, y: 0, width: self.tableView.bounds.size.width, height: self.tableView.bounds.size.height))
            noDataLabel.text          = "No data available"
            noDataLabel.textColor     = UIColor.black
            noDataLabel.textAlignment = .center
            self.tableView.backgroundView  = noDataLabel
            self.tableView.separatorStyle  = .none
        }
        return numOfSections
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        return packs.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell:UITableViewCell = tableView.dequeueReusableCell(withIdentifier: "cell", for: indexPath)
        let rervationcell: ReservationModel!
        rervationcell = self.reservations[indexPath.row]
        let packcell: PackModel!
        packcell = self.packs[indexPath.row]
        
        
        
        
        let imageURL:UIImageView = cell.viewWithTag(5) as! UIImageView
        //imageURL.image = UIImage(data: data! as Data)
        imageURL.image = #imageLiteral(resourceName: "travel")
        
        
        
        let lblArrive:UILabel = cell.viewWithTag(1) as! UILabel
        lblArrive.text = packcell.nomDepart
        
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        formatter.string(from: (packcell.dateFin!))
        let date_d = formatter.string(from: (packcell.dateDebut!)).substring(to:formatter.string(from: (packcell.dateDebut!)).index(formatter.string(from: (packcell.dateDebut!)).startIndex, offsetBy: 10))
        let date_f = formatter.string(from: (packcell.dateFin!)).substring(to:formatter.string(from: (packcell.dateFin!)).index(formatter.string(from: (packcell.dateFin!)).startIndex, offsetBy: 10))
        let lblDateDepart:UILabel = cell.viewWithTag(2) as! UILabel
        lblDateDepart.text = date_d
        
        let lblDateArrive:UILabel = cell.viewWithTag(3) as! UILabel
        lblDateArrive.text = date_f
        
        let statu:UIImageView = cell.viewWithTag(4) as! UIImageView
        
        let lblstatu:UILabel = cell.viewWithTag(6) as! UILabel
        
        if rervationcell.etat == "1" {
            statu.image = #imageLiteral(resourceName: "accept")
            lblstatu.text = "Accepted"
        }else if (rervationcell.etat == "0")  {
            statu.image = #imageLiteral(resourceName: "denied")
            lblstatu.text = "Denied"
        }else{
            statu.image = #imageLiteral(resourceName: "wating")
            lblstatu.text = "Waiting"
        }
        
        return cell
    }
    
    
    
    
    
    
    
    func UIColorFromRGB(rgbValue: UInt) -> UIColor {
        return UIColor(
            red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
            green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
            blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
            alpha: CGFloat(1.0)
        )
    }
    
    
    
    func tableView(_ tableView: UITableView, editActionsForRowAt indexPath: IndexPath) -> [UITableViewRowAction]? {
        let delete = UITableViewRowAction(style: .destructive, title: "Youtube") { (action, indexPath) in
            SweetAlert().showAlert("Are you sure?", subTitle: "This will open youtube app!", style: AlertStyle.warning, buttonTitle:"No, cancel!", buttonColor:self.UIColorFromRGB(rgbValue: 0xD0D0D0) , otherButtonTitle:  "Yes, open it!", otherButtonColor: self.UIColorFromRGB(rgbValue: 0xDD6B55)) { (isOtherButton) -> Void in
                if isOtherButton == true {
                    
                    _ = SweetAlert().showAlert("Cancelled!", subTitle: "Action cancelled", style: AlertStyle.error)
                }
                else {
                    let ii = self.packs[indexPath.item]
                    //UIApplication.shared.openURL(URL(string: "http://youtube.com")!)
                    let YoutubeID =  ii.nomDepart // Your Youtube ID here
                    
                    let appURL = NSURL(string: "youtube://www.youtube.com/results?search_query=travel%20to%20"+YoutubeID!)!
                    
                    let webURL = NSURL(string: "https://www.youtube.com/results?search_query=travel%20to%20"+YoutubeID!)!
                    
                    
                    let application = UIApplication.shared
                    
                    if application.canOpenURL(appURL as URL) {
                        application.open(appURL as URL)
                    } else {
                        // if Youtube app is not installed, open URL inside Safari
                        application.open(webURL as URL)
                    }
                    
                }
            }
            
            
            
            
        }
        
        
        
        
        
        
        
        
        
        
        
        let share = UITableViewRowAction(style: .normal, title: "Instagram") { (action, indexPath) in
            
            
            
            
            SweetAlert().showAlert("Are you sure?", subTitle: "This will open instagram app!", style: AlertStyle.warning, buttonTitle:"No, cancel!", buttonColor:self.UIColorFromRGB(rgbValue: 0xD0D0D0) , otherButtonTitle:  "Yes, open it!", otherButtonColor: self.UIColorFromRGB(rgbValue: 0xDD6B55)) { (isOtherButton) -> Void in
                if isOtherButton == true {
                    
                    _ = SweetAlert().showAlert("Cancelled!", subTitle: "Action cancelled", style: AlertStyle.error)
                }
                else {
                    let ii = self.packs[indexPath.item]
                    //UIApplication.shared.openURL(URL(string: "http://youtube.com")!)
                    let YoutubeID =  ii.nomDepart // Your Youtube ID here
                    
                    let appURL = NSURL(string: "youtube://www.instagram.com/explore/tags/"+YoutubeID!+"travel")!
                    
                    let webURL = NSURL(string: "https://www.instagram.com/explore/tags/"+YoutubeID!+"travel")!
                    
                    
                    let application = UIApplication.shared
                    
                    if application.canOpenURL(appURL as URL) {
                        application.open(appURL as URL)
                    } else {
                        // if Youtube app is not installed, open URL inside Safari
                        application.open(webURL as URL)
                    }
                    
                }
            }
            
            
            
            
            
            
            
        }
        
        share.backgroundColor = UIColor.violetred
        
        return [delete, share]
    }
    
    
    
    
    
    
    
}

