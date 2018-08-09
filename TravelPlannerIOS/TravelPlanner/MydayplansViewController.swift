//
//  MydayplansViewController.swift
//  TravelPlanner
//
//  Created by ESPRIT on 12/12/2017.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//

import UIKit
import Alamofire
import CRRefresh
class MydayplansViewController: UIViewController ,UITableViewDelegate,UITableViewDataSource{
    
    @IBOutlet weak var tableview: UITableView!
    
    @IBOutlet weak var load: UIActivityIndicatorView!
    var feedItems = [Dictionary<String, AnyObject>]()
    override func viewWillAppear(_ animated: Bool) {
        
    
        
        
        if Connectivity.isConnectedToInternet() == true {
            
            load.startAnimating()
            
            feedItems.removeAll()
            CalendarViewController.list1.removeAll()
            CalendarViewController.list.removeAll()
            let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec]
            let  x = String(describing: SignUpViewController.connecteduser.idUser!)
            let urlPath = URL(string: NavigatorData.URL+"/api/Getdayplans?id="+x)
            
            Alamofire.request(urlPath!,method:.post,headers: Auth_header).responseJSON { responsep in
                if(responsep.result.isSuccess){
                    // let userm = UserModel()
                    
                    let dictp = responsep.result.value! as? Dictionary<String, AnyObject>
                    let dictp1 = dictp?["dayplans"] as? NSArray
                    
                    if(dictp1 != nil){
                        
                        
                        for places1 in (dictp1! as NSArray) {
                            let place0 = places1 as? NSArray
                            for places in (place0! as NSArray ) {
                                let place = places as? Dictionary<String, AnyObject>
                                self.feedItems.append(place!)
                                
                                self.tableview.reloadData()
                            }
                        }
                    }
                    if(self.feedItems.count == 0){
                      _ =  SweetAlert().showAlert("Error!", subTitle: "You have no day plans!", style: AlertStyle.error)
                    }
                    
                    
                    self.load.isHidden = true
                    
                    
                    
                }else if(responsep.result.isFailure){
                    print("le le")
                }
                
            }
            
            
            
            
            
            
            
            
            let Auth_header1    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec]
            let urlPath1 = URL(string: NavigatorData.URL+"/api/Getdayplanscal?id="+x)
            
            Alamofire.request(urlPath1!,method:.post,headers: Auth_header1).responseJSON { responsep in
                if(responsep.result.isSuccess){
                    // let userm = UserModel()
                    
                    let dictp = responsep.result.value! as? Dictionary<String, AnyObject>
                    let dictp1 = dictp?["dates"] as? NSArray
                    let dictp2 = dictp?["titles"] as? NSArray
                    
                    if(dictp1 != nil){
                    for places1 in (dictp2! as NSArray) {
                        let place1 = places1 as? String
                        CalendarViewController.list1.append(place1!)
                        
                    }
                    for places in (dictp1! as NSArray) {
                        let place = places as? String
                        CalendarViewController.list.append(place!)
                        
                    }
                    }
                    
                }else if(responsep.result.isFailure){
                    print("le le")
                }
                
            }
            
            
            tableview.cr.addHeadRefresh(animator: FastAnimator()) { [weak self] in
                self?.feedItems.removeAll()
                CalendarViewController.list1.removeAll()
                CalendarViewController.list.removeAll()
                
                self?.feedItems.removeAll()
                let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec]
                let  x = String(describing: SignUpViewController.connecteduser.idUser!)
                let urlPath = URL(string: NavigatorData.URL+"/api/Getdayplans?id="+x)
                
                Alamofire.request(urlPath!,method:.post,headers: Auth_header).responseJSON { responsep in
                    if(responsep.result.isSuccess){
                        // let userm = UserModel()
                        
                        let dictp = responsep.result.value! as? Dictionary<String, AnyObject>
                        let dictp1 = dictp?["dayplans"] as? NSArray
                        
                        if(dictp1 != nil){
                            
                            
                            for places1 in (dictp1! as NSArray) {
                                let place0 = places1 as? NSArray
                                for places in (place0! as NSArray) {
                                    let place = places as? Dictionary<String, AnyObject>
                                    self?.feedItems.append(place!)
                                    
                                    self?.tableview.reloadData()
                                }
                            }
                            
                        }
                        if(self?.feedItems.count == 0){
                          _ =  SweetAlert().showAlert("Error!", subTitle: "You have no day plans!", style: AlertStyle.error)
                        }
                        
                        self?.tableview.cr.endHeaderRefresh()
                        
                        
                        
                    }else if(responsep.result.isFailure){
                        print("le le")
                    }
                    
                }
                
                
                
                
                
                
                
                
                let Auth_header1    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec]
                let urlPath1 = URL(string: NavigatorData.URL+"/api/Getdayplanscal?id="+x)
                
                Alamofire.request(urlPath1!,method:.post,headers: Auth_header1).responseJSON { responsep in
                    if(responsep.result.isSuccess){
                        // let userm = UserModel()
                        
                        let dictp = responsep.result.value! as? Dictionary<String, AnyObject>
                        let dictp1 = dictp?["dates"] as? NSArray
                        let dictp2 = dictp?["titles"] as? NSArray
                        
                        if(dictp1 != nil){
                        for places1 in (dictp2! as NSArray) {
                            let place1 = places1 as? String
                            CalendarViewController.list1.append(place1!)
                            
                            
                        }
                        for places in (dictp1! as NSArray) {
                            let place = places as? String
                            CalendarViewController.list.append(place!)
                            
                            
                        }
                        }
                        
                        
                        
                        
                    }else if(responsep.result.isFailure){
                        print("le le")
                    }
                    
                    
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
        
        
        
        
        
        // Do any additional setup after loading the view.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func UIColorFromRGB(rgbValue: UInt) -> UIColor {
        return UIColor(
            red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
            green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
            blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
            alpha: CGFloat(1.0)
        )
    }
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return feedItems.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell:UITableViewCell = tableView.dequeueReusableCell(withIdentifier: "mydaycell", for: indexPath)
        
        let nearby = self.feedItems[indexPath.row]
            as Dictionary<String, AnyObject>
        
        print(nearby)
        let lblName:UILabel = cell.viewWithTag(1) as! UILabel
        lblName.text = nearby["title"] as! String?
        let lblcategorie:UILabel = cell.viewWithTag(2) as! UILabel
        lblcategorie.text = nearby["date"] as! String?
        
        let lblctext:UILabel = cell.viewWithTag(3) as! UILabel
        lblctext.text = nearby["description"] as! String?
        
        
        
        
        return cell
        
        
    }
    @IBAction func goback(_ sender: Any) {
        let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let newViewController = storyBoard.instantiateViewController(withIdentifier: "Accueil") as! UINavigationController
        self.present(newViewController, animated: true, completion: nil)
    }


}
