//
//  ListourpacksViewController.swift
//  TravelPlanner
//
//  Created by ESPRIT on 14/12/17.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//

import UIKit
import Alamofire
import CRRefresh
import CoreData
class ListourpacksViewController: UIViewController  ,UITableViewDelegate,UITableViewDataSource{

    var people:[NSManagedObject] = []
    var feedItems = [Dictionary<String, AnyObject>]()
    var paysss = [NSArray]()
    
    @IBOutlet weak var listeview: UITableView!
    
    
    @IBOutlet weak var load: UIActivityIndicatorView!
    override func viewWillAppear(_ animated: Bool) {
        
        
        if Connectivity.isConnectedToInternet() == true {
            
            load.startAnimating()
            
            feedItems.removeAll()
            let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec]
            let urlPath = URL(string: NavigatorData.URL+"/api/Getallpacks")
            
            Alamofire.request(urlPath!,method:.post,headers: Auth_header).responseJSON { responsep in
                if(responsep.result.isSuccess){
                    // let userm = UserModel()
                    
                    let dictp = responsep.result.value! as? Dictionary<String, AnyObject>
                    
                    
                    let dictp1 = dictp?["packs"] as? NSArray
                    let dictp00 = dictp?["pays"] as? NSArray
                    
                    if(dictp1 != nil){
                        
                        
                        for places in (dictp1! as NSArray) {
                            let place = places as? Dictionary<String, AnyObject>
                            self.feedItems.append(place!)
                            self.listeview.reloadData()
                        }
                        
                        for placesff in (dictp00! as NSArray) {
                            let place0 = placesff as? NSArray
                            self.paysss.append(place0!)
                            self.listeview.reloadData()
                        }
                    }
                    
                    
                    
                    
                    if(self.feedItems.count == 0){
                     _ =   SweetAlert().showAlert("Error!", subTitle: "You have no packs!", style: AlertStyle.error)
                    }
                    self.load.isHidden = true
                    
                    
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
        
        listeview.cr.addHeadRefresh(animator: FastAnimator()) { [weak self] in
        self?.feedItems.removeAll()
       
            
            let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec]
            let urlPath = URL(string: NavigatorData.URL+"/api/Getallpacks")
            
            Alamofire.request(urlPath!,method:.post,headers: Auth_header).responseJSON { responsep in
                if(responsep.result.isSuccess){
                    // let userm = UserModel()
                    
                    let dictp = responsep.result.value! as? Dictionary<String, AnyObject>
                    
                    
                    let dictp1 = dictp?["packs"] as? NSArray
                    let dictp00 = dictp?["pays"] as? NSArray
                    
                    if(dictp1 != nil){
                        
                        
                        for places in (dictp1! as NSArray) {
                            let place = places as? Dictionary<String, AnyObject>
                            self?.feedItems.append(place!)
                            self?.listeview.reloadData()
                        }
                        
                        for placesff in (dictp00! as NSArray) {
                            let place0 = placesff as? NSArray
                            self?.paysss.append(place0!)
                            self?.listeview.reloadData()
                        }
                    }
                    
                    self?.listeview.cr.endHeaderRefresh()
                    
                    
                    if(self?.feedItems.count == 0){
                       _ = SweetAlert().showAlert("Error!", subTitle: "You have no packs!", style: AlertStyle.error)
                    }
                    self?.load.isHidden = true
                    
                    
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
        
        let cell:UITableViewCell = tableView.dequeueReusableCell(withIdentifier: "affpackcell", for: indexPath)
        
        let nearby = self.feedItems[indexPath.row]
            as Dictionary<String, AnyObject>
        
        let x1 = nearby["date_debut"] as! String?
        let x11 = x1?.substring(to:(x1?.index((x1?.startIndex)!, offsetBy: 10))!)
        let y1 = nearby["date_fin"] as! String?
        let y11 = y1?.substring(to:(y1?.index((y1?.startIndex)!, offsetBy: 10))!)
        
        
        let lblName:UILabel = cell.viewWithTag(1) as! UILabel
        lblName.text = x11!+" => "+y11!
        let lblcategorie:UILabel = cell.viewWithTag(2) as! UILabel
        
        let nearby00 = self.paysss[indexPath.row]
            as NSArray
        print(nearby00)
        
        var ff2 = ""
        var f = 0
        for places in (nearby00 as NSArray) {
            let ff1 = places as? Dictionary<String, AnyObject>
            let bbb = (ff1?["nom_pays"] as! String?)!
            if(f == 0){
                ff2 = bbb
                }else{
            
            ff2 = ff2+" ,"+bbb
                }
            f = f+1
        }
        
        
        
        
        
       
        
        
        let ff = nearby["nom_depart"] as! String?
        lblcategorie.text = ff!+"=>"+ff2
        
        let lblctext:UILabel = cell.viewWithTag(3) as! UILabel
        let x = nearby["prix"] as! NSNumber?
        let o1 = String(describing: x!)
        lblctext.text = "\(o1) Dt"
        
        
        
        return cell
        
        
    }

    @IBAction func logout(_ sender: Any) {
        let urlPath = URL(string: NavigatorData.URL+"/api/logout?token="+SignUpViewController.tokensec)
        Alamofire.request(urlPath!,method:.get ).responseJSON { responsep in
            if(responsep.result.isSuccess){
                let context = ( UIApplication.shared.delegate as! AppDelegate ).persistentContainer.viewContext
                let deleteFetch = NSFetchRequest<NSFetchRequestResult>(entityName: "User")
                let deleteRequest = NSBatchDeleteRequest(fetchRequest: deleteFetch)
                do
                {
                    try context.execute(deleteRequest)
                    try context.save()
                    print("fasa5t")
                }
                catch
                {
                    print ("There was an error")
                }
                self.save(name: SignUpViewController.connecteduser.username!, pwd: "")
                
                
                
                
                let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
                let newViewController = storyBoard.instantiateViewController(withIdentifier: "login")
                self.present(newViewController, animated: true, completion: nil)
            }else if(responsep.result.isFailure){
                print("le le")
            }
            
        }
    }
    func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        return true
    }
    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
       
        SweetAlert().showAlert("Are you sure?", subTitle: "The pack will permanently delete!", style: AlertStyle.warning, buttonTitle:"No, cancel!", buttonColor:UIColorFromRGB(rgbValue: 0xD0D0D0) , otherButtonTitle:  "Yes, delete it!", otherButtonColor: UIColorFromRGB(rgbValue: 0xDD6B55)) { (isOtherButton) -> Void in
            if isOtherButton == true {
                
              _ =  SweetAlert().showAlert("Cancelled!", subTitle: "Pack is safe", style: AlertStyle.error)
            }
            else {
                
                let ii = self.feedItems[indexPath.item]
                let ff = String(describing: ii["id"] as! Int)
                self.self.feedItems.remove(at: indexPath.item)
                self.listeview.deleteRows(at: [indexPath], with: .automatic)
                
                let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec]
                
                
                let urlPath = URL(string: NavigatorData.URL+"/api/Deletemypack?id="+ff)
                
                Alamofire.request(urlPath!,method:.post,headers: Auth_header).responseJSON { responsep in
                    if(responsep.result.isSuccess){
                        
                        
                        
                    }else if(responsep.result.isFailure){
                        print("le le")
                    }
                    
                }
              _ =  SweetAlert().showAlert("Deleted!", subTitle: "Pack has been deleted!", style: AlertStyle.success)
            }
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
    func save(name: String,pwd: String) {
        
        guard let appDelegate =
            UIApplication.shared.delegate as? AppDelegate else {
                return
        }
        
        // 1
        let managedContext =
            appDelegate.persistentContainer.viewContext
        
        // 2
        let entity =
            NSEntityDescription.entity(forEntityName: "User",
                                       in: managedContext)!
        
        let person = NSManagedObject(entity: entity,
                                     insertInto: managedContext)
        
        // 3
        person.setValue(name, forKeyPath: "username")
        person.setValue(pwd, forKeyPath: "password")
        
        // 4
        do {
            try managedContext.save()
            people.append(person)
        } catch let error as NSError {
            print("Could not save. \(error), \(error.userInfo)")
        }
    }
}
