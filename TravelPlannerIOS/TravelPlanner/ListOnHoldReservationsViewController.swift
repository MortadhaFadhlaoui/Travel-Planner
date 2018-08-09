//
//  ListOnHoldReservationsViewController.swift
//  TravelPlanner
//
//  Created by Ahmed Mohamed on 09/01/2018.
//  Copyright © 2018 AhmedMohamed. All rights reserved.
//

import UIKit
import Alamofire
import CRRefresh
class ListOnHoldReservationsViewController: UIViewController ,UITableViewDelegate,UITableViewDataSource{

    @IBOutlet weak var loader: UIActivityIndicatorView!
    @IBOutlet weak var listview: UITableView!
    
    var feedItems = [Dictionary<String, AnyObject>]()
    var paysss = [NSArray]()
    var packs = [NSArray]()
    
    
    override func viewWillAppear(_ animated: Bool) {
        
        
        
        if Connectivity.isConnectedToInternet() == true {
            loader.startAnimating()
            
            
            feedItems.removeAll()
            self.navigationItem.setHidesBackButton(true, animated:true);
            let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec]
            let urlPath = URL(string: NavigatorData.URL+"/api/Getonholdres")
            
            Alamofire.request(urlPath!,method:.get,headers: Auth_header).responseJSON { responsep in
                if(responsep.result.isSuccess){
                    // let userm = UserModel()
                    
                    let dictp = responsep.result.value! as? Dictionary<String, AnyObject>
                    let dictp1 = dictp?["res"] as? NSArray
                    
                    if(dictp1 != nil){
                        
                        
                        
                        for places in (dictp1! as NSArray) {
                            let place = places as? Dictionary<String, AnyObject>
                            self.feedItems.append(place!)
                            
                        }
                        
                        let dictp00 = dictp?["users"] as? NSArray
                        
                        for placesff in (dictp00! as NSArray) {
                            let place0 = placesff as? NSArray
                            
                            self.paysss.append(place0!)
                            
                        }
                        
                        let dictp000 = dictp?["packs"] as? NSArray
                        for placesfff in (dictp000! as NSArray) {
                            let place00 = placesfff as? NSArray
                            
                            self.packs.append(place00!)
                            self.listview.reloadData()
                            
                        }
                    }
                    if(self.feedItems.count == 0){
                      _ =  SweetAlert().showAlert("Error!", subTitle: "You have no on hold reservations!", style: AlertStyle.error)
                    }
                    self.loader.isHidden = true
                    
                    
                    
                }else if(responsep.result.isFailure){
                    print("le le")
                }
                
            }
            
        }else{
            
            SweetAlert().showAlert("Conncetion error?", subTitle: "You have no internet acces!", style: AlertStyle.warning, buttonTitle:"No, cancel!", buttonColor:UIColorFromRGB(rgbValue: 0xD0D0D0) , otherButtonTitle:  "Enable WIFI!", otherButtonColor: UIColorFromRGB(rgbValue: 0xDD6B55)) { (isOtherButton) -> Void in
                if isOtherButton == true {
                    let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
                    let newViewController = storyBoard.instantiateViewController(withIdentifier: "login")
                    self.present(newViewController, animated: true, completion: nil)
                    
                }
                else {
                    let url = URL(string: "App-Prefs:root=WIFI") //for WIFI setting app
                    let app = UIApplication.shared
                    app.open(url!, options: [:], completionHandler: nil)
                    
                }
            }
            
            
            
            
            
            
        }
        
        
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        listview.cr.addHeadRefresh(animator: FastAnimator()) { [weak self] in
            
            self?.feedItems.removeAll()
            let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec]
            let urlPath = URL(string: NavigatorData.URL+"/api/Getonholdres")
            
            Alamofire.request(urlPath!,method:.get,headers: Auth_header).responseJSON { responsep in
                if(responsep.result.isSuccess){
                    // let userm = UserModel()
                    
                    let dictp = responsep.result.value! as? Dictionary<String, AnyObject>
                    let dictp1 = dictp?["res"] as? NSArray
                    
                    
                    if(dictp1 != nil){
                        
                    
                    for places in (dictp1! as NSArray) {
                        let place = places as? Dictionary<String, AnyObject>
                        self?.feedItems.append(place!)
                        
                    }
                    
                    let dictp00 = dictp?["users"] as? NSArray
                    
                    
                    
                    for placesff in (dictp00! as NSArray) {
                        let place0 = placesff as? NSArray

                        self?.paysss.append(place0!)
                        
                    }
  
                    let dictp000 = dictp?["packs"] as? NSArray

                    for placesfff in (dictp000! as NSArray) {
                        let place00 = placesfff as? NSArray

                        self?.packs.append(place00!)
                        self?.listview.reloadData()
                        
                    }
                    }
                    if(self?.feedItems.count == 0){
                      _ =  SweetAlert().showAlert("Error!", subTitle: "You have no on hold reservations!", style: AlertStyle.error)
                    }
                    
                    self?.listview.layoutIfNeeded()
                    self?.listview.beginUpdates()
                    self?.listview.endUpdates()
                    self?.listview.cr.endHeaderRefresh()
                    
                }else if(responsep.result.isFailure){
                    print("le le")
                }
                
            }
            
            }
        
        
        

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return feedItems.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell:UITableViewCell = tableView.dequeueReusableCell(withIdentifier: "reservationonhold", for: indexPath)
        
        let nearby = self.feedItems[indexPath.row]
            as Dictionary<String, AnyObject>
        
        
        let lblName:UILabel = cell.viewWithTag(1) as! UILabel
        let hh = nearby["date"] as! String?
        let xy1 = hh?.substring(to:(hh?.index((hh?.startIndex)!, offsetBy: 10))!)
        lblName.text = xy1
        
        
        let nearby00 = self.paysss[indexPath.row]
            as NSArray
        let ff1 = nearby00[0] as? Dictionary<String, AnyObject>
        
        
        
        let lblcategorie:UILabel = cell.viewWithTag(2) as! UILabel
        let xxx1 = ff1?["username"] as! String?
        let xxx2 = ff1?["email"] as! String?
        lblcategorie.text = xxx1!+"=>"+xxx2!
        
        
        let nearby000 = self.packs[indexPath.row]
            as NSArray
        let fff1 = nearby000[0] as? Dictionary<String, AnyObject>
        
        
        
        let lblctext:UILabel = cell.viewWithTag(3) as! UILabel
        let xxxx1 = fff1?["nom_depart"] as! String?
        let xxxx2 = fff1?["prix"] as! NSNumber?
        let xxxxx2 = String(describing: xxxx2!)
        lblctext.text = xxxx1!+"=>"+xxxxx2+" Dt"
        
        
        
        return cell
        
        
    }


    func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        return true
    }
    
    
    
    @IBAction func back(_ sender: Any) {
        let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let newViewController = storyBoard.instantiateViewController(withIdentifier: "agenthome") as! UITabBarController
        self.present(newViewController, animated: true, completion: nil)
    }
    func tableView(_ tableView: UITableView, editActionsForRowAt indexPath: IndexPath) -> [UITableViewRowAction]? {
    
        let delete = UITableViewRowAction(style: .destructive, title: "Reject") { (action, indexPath) in
            
            SweetAlert().showAlert("Are you sure?", subTitle: "The reservation will permanently rejected!", style: AlertStyle.warning, buttonTitle:"No, cancel!", buttonColor:self.UIColorFromRGB(rgbValue: 0xD0D0D0) , otherButtonTitle:  "Yes, reject it!", otherButtonColor: self.UIColorFromRGB(rgbValue: 0xDD6B55)) { (isOtherButton) -> Void in
                if isOtherButton == true {
                    
                   _ = SweetAlert().showAlert("Cancelled!", subTitle: "reservation is on hold", style: AlertStyle.error)
                }
                else {
                    let ii = self.feedItems[indexPath.item]
                    let ff = String(describing: ii["id"] as! Int)
                    print(ff)
                    self.feedItems.remove(at: indexPath.item)
                    self.paysss.remove(at: indexPath.item)
                    self.packs.remove(at: indexPath.item)
                    self.listview.deleteRows(at: [indexPath], with: .automatic)
                    
                    let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec]
                    
                    
                    let urlPath = URL(string: NavigatorData.URL+"/api/deletereservation?id="+ff)
                    print(ff)
                    Alamofire.request(urlPath!,method:.get,headers: Auth_header).responseJSON { responsep in
                        if(responsep.result.isSuccess){
                            
                            
                            
                        }else if(responsep.result.isFailure){
                            print("le le")
                        }
                        
                    }
                    _ = SweetAlert().showAlert("Rejected!", subTitle: "reservation has been rejected!", style: AlertStyle.success)
                }
            }
            

            
        }
        
        
        
        
        let share = UITableViewRowAction(style: .normal, title: "Accept") { (action, indexPath) in
            
            SweetAlert().showAlert("Are you sure?", subTitle: "The reservation will permanently accepted!", style: AlertStyle.warning, buttonTitle:"No, cancel!", buttonColor:self.UIColorFromRGB(rgbValue: 0xD0D0D0) , otherButtonTitle:  "Yes, accept it!", otherButtonColor: self.UIColorFromRGB(rgbValue: 0xDD6B55)) { (isOtherButton) -> Void in
                if isOtherButton == true {
                    
                   _ = SweetAlert().showAlert("Cancelled!", subTitle: "reservation is on hold", style: AlertStyle.error)
                }
                else {
                    
                    let ii = self.feedItems[indexPath.item]
                    let ff = String(describing: ii["id"] as! Int)
                    print(ff)
                    self.feedItems.remove(at: indexPath.item)
                    self.paysss.remove(at: indexPath.item)
                    self.packs.remove(at: indexPath.item)
                    self.listview.deleteRows(at: [indexPath], with: .automatic)
                    
                    let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec]
                    
                    
                    let urlPath = URL(string: NavigatorData.URL+"/api/acceptreservation?id="+ff)
                    print(ff)
                    Alamofire.request(urlPath!,method:.get,headers: Auth_header).responseJSON { responsep in
                        if(responsep.result.isSuccess){
                            
                            
                            
                        }else if(responsep.result.isFailure){
                            print("le le")
                        }
                        
                    }
                    
                   _ = SweetAlert().showAlert("Accepted!", subTitle: "Reservation has been accepted!", style: AlertStyle.success)
                }
            }
    
            
            
            
            
            
            
            
            
        }
        
        share.backgroundColor = UIColor.seagreen
        
        return [delete, share]
    }
    func UIColorFromRGB(rgbValue: UInt) -> UIColor {
        return UIColor(
            red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
            green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
            blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
            alpha: CGFloat(1.0)
        )
    }
}
