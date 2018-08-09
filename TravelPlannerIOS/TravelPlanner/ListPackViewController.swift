//
//  ListPackViewController.swift
//  TravelPlanner
//
//  Created by Choura Yessine on 4/8/18.
//  Copyright © 2018 AhmedMohamed. All rights reserved.
//

import UIKit
import Alamofire
class ListPackViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
 var uniquePosts:[PackModel] = []
 var paysItemssarray : [[PaysModel]]  = []
 var packPosts:[PackModel] = []
    var pacPosts:[PackModel] = []
     var dayplanpack: DayPlanerViewController?
    @IBOutlet weak var tableView: UITableView!
    
    
var paysItemss : [PaysModel]  = []
    override func viewDidLoad() {
        super.viewDidLoad()

            if Connectivity.isConnectedToInternet() {
            
            let urlPath = URL(string: NavigatorData.URL+"/api/getPacks")
            
            let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec ]
            
                let parameters = [:] as [String : Any]
            
            Alamofire.request(urlPath!, method: .post,parameters: parameters,encoding: JSONEncoding.default,headers: Auth_header ).responseJSON { responsep in
                if(responsep.result.isSuccess){
                    let dict = responsep.result.value as? Dictionary<String, AnyObject>
                    let success = dict?["success"]! as! NSNumber
                    
                    if(success==1){
                    let packs = dict?["packs"] as? NSArray
                        self.paysItemss.removeAll()
                        self.packPosts.removeAll()
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
                            //print(item)
                            let pays = item?["pays"] as? NSArray
                            for item in (pays! as NSArray) {
                                 let itemm = item as? Dictionary<String, AnyObject>
                                let idPays = itemm!["id"] as! Int
                                let nom_pays = itemm!["nom_pays"] as! String
                                let paysm = PaysModel()
                                paysm.idPays = idPays as Int?
                                paysm.nomPays = nom_pays
                                 paysm.Pack = packm
                                
                                self.paysItemss.append(paysm)
                                  self.tableView.reloadData()
                             }
                           
                            self.packPosts.append(packm)
                            self.pacPosts.append(packm)
        
                            self.tableView.reloadData()
                            
                        }
                        self.packPosts.forEach { (p) -> () in
            if !self.uniquePosts.contains (where: { $0.idPack == p.idPack }) {
                self.uniquePosts.append(p)
            }
        }
    
        for row in 0..<self.uniquePosts.count {
            // Append an empty row.
            self.paysItemssarray.append([PaysModel]())
            for i in 0..<self.paysItemss.count {
                // Populate the row.
                if self.paysItemss[i].Pack?.idPack == self.uniquePosts[row].idPack {
                    self.paysItemssarray[row].append(self.paysItemss[i])
                }
            }
        }
                       self.tableView.reloadData()
                        
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
            print(self.packPosts)
   
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

     func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let view = UIView()
        
        view.backgroundColor = UIColor(red: 0/255, green: 154/255, blue: 154/255, alpha: 0.8)
        let label = UILabel()
        
        let x1 = String(describing: (uniquePosts[section].prix)!)
        label.text = "This Pack for \(x1)$"
        label.frame = CGRect(x: 10, y: 5, width: 300, height: 35)
        
        view.addSubview(label)
        return view
    }
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 50
    }

    
    func numberOfSections(in tableView: UITableView) -> Int {
        return uniquePosts.count
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
  
        return paysItemssarray[section].count
        
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell:UITableViewCell = tableView.dequeueReusableCell(withIdentifier: "cell", for: indexPath)
        let pays: PaysModel!
     
           pays = paysItemssarray[indexPath.section][indexPath.row]
        
      
        
        let urlPath = URL(string: "https://pixabay.com/api/?key=7171796-c3b099ec431290658803b333a&q=\(pays.nomPays!)&image_type=photo")
        Alamofire.request(urlPath!,method:.post).responseJSON { responsep in
            if(responsep.result.isSuccess){
                let dictp = responsep.result.value! as? Dictionary<String, AnyObject>
                let groups = dictp?["hits"] as? NSArray
                var array = [String]()
                for places in (groups! as NSArray) {
                    let obj = places as? Dictionary<String, AnyObject>
                    array.append(obj!["webformatURL"] as! String)
                    
                }
                /*let url = NSURL(string: array[0])
                let data = NSData(contentsOf:url! as URL)*/
                
                let imageURL:UIImageView = cell.viewWithTag(1) as! UIImageView
                //imageURL.image = UIImage(data: data! as Data)
                imageURL.sd_setImage(with: URL(string: array[0]), placeholderImage: UIImage(named: "placeholder.png"))
            }else if(responsep.result.isFailure){
                let imageURL:UIImageView = cell.viewWithTag(1) as! UIImageView
                //imageURL.image = UIImage(data: data! as Data)
                imageURL.image = #imageLiteral(resourceName: "placewithno")
            }
            
        }
        
        
        let lblArrive:UILabel = cell.viewWithTag(2) as! UILabel
        lblArrive.text = pays.nomPays
        
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        formatter.string(from: (pays.Pack?.dateFin!)!)
        let date_d = formatter.string(from: (pays.Pack?.dateDebut!)!).substring(to:formatter.string(from: (pays.Pack?.dateDebut!)!).index(formatter.string(from: (pays.Pack?.dateDebut!)!).startIndex, offsetBy: 10))
        let date_f = formatter.string(from: (pays.Pack?.dateFin!)!).substring(to:formatter.string(from: (pays.Pack?.dateFin!)!).index(formatter.string(from: (pays.Pack?.dateFin!)!).startIndex, offsetBy: 10))
        let lblDateDepart:UILabel = cell.viewWithTag(3) as! UILabel
        lblDateDepart.text = date_d
        
        let lblDateArrive:UILabel = cell.viewWithTag(4) as! UILabel
        lblDateArrive.text = date_f

        return cell
    }
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "dayplansho" {
            let indexrow : Int = (tableView.indexPathForSelectedRow?.row)!
            let indexsection : Int = (tableView.indexPathForSelectedRow?.section)!
            self.dayplanpack = segue.destination as? DayPlanerViewController
            self.dayplanpack?.payspack = paysItemssarray[indexsection][indexrow]
            
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
       func UIColorFromRGB(rgbValue: UInt) -> UIColor {
        return UIColor(
            red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
            green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
            blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
            alpha: CGFloat(1.0)
        )
    }

}
