//
//  DetailsNearbyViewController.swift
//  TravelPlanner
//
//  Created by ESPRIT on 23/11/2017.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//

import UIKit
import KCFloatingActionButton
import Alamofire
class DetailsNearbyViewController: UIViewController,KCFloatingActionButtonDelegate     {
    
    var nearby = NearbyModel()
    
    @IBOutlet weak var Image: UIImageView!
    @IBOutlet weak var Distance: UILabel!
    @IBOutlet weak var Name: UILabel!
    @IBOutlet weak var Categorie: UILabel!
    
    @IBOutlet weak var addrese: UILabel!
    
    @IBOutlet weak var imagemap: UIImageView!
    @IBOutlet weak var tips: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        if Connectivity.isConnectedToInternet() {
            Image.isHidden = false
            Distance.isHidden = false
            Name.isHidden = false
            Categorie.isHidden = false
            addrese.isHidden = false
            tips.isHidden = false
            imagemap.isHidden = false
            
            let staticMapUrl: String = "https://maps.googleapis.com/maps/api/staticmap?center=\(String(describing: nearby.lat!)),\(String(describing: nearby.lng!))&zoom=14&size=300x150&markers=color:black%7C\(String(describing: nearby.lat!)),\(String(describing: nearby.lng!))&key=AIzaSyBnDnXfY-nd7BCDlUId78LDFXtFcfhEn0Y"
            
            /*let url = NSURL(string: staticMapUrl)
             let data = NSData(contentsOf:url! as URL)*/
            imagemap.sd_setImage(with: URL(string: staticMapUrl), placeholderImage: UIImage(named: "placeholder.png"))
            //imagemap.image = UIImage(data: data! as Data)
            
            if nearby.image == nil {
                Image.image = #imageLiteral(resourceName: "flou-gaussien")
            }else{
                /*let a = (nearby.image)! as String
                 let url = NSURL(string: a)
                 let data = NSData(contentsOf:url! as URL)*/
                Image.sd_setImage(with: URL(string: nearby.image!), placeholderImage: UIImage(named: "placeholder.png"))
                // Image.image = UIImage(data: data! as Data)
            }
            //
            //Name.text = nearby.name
            Categorie.text = nearby.categorie
            
            if nearby.isOpen?.description == "false" {
                let imageAttachment =  NSTextAttachment()
                imageAttachment.image = UIImage(named:"closemorta")
                //Set bound to reposition
                let imageOffsetY:CGFloat = -5.0;
                imageAttachment.bounds = CGRect(x: 0, y: imageOffsetY, width: imageAttachment.image!.size.width, height: imageAttachment.image!.size.height)
                //Create string with attachment
                let attachmentString = NSAttributedString(attachment: imageAttachment)
                //Initialize mutable string
                let completeText = NSMutableAttributedString(string: nearby.name!+"  ")
                //Add image to mutable string
                completeText.append(attachmentString)
                //Add your text to mutable string
                let  textAfterIcon = NSMutableAttributedString(string: "")
                completeText.append(textAfterIcon)
                Name.textAlignment = .center;
                Name.attributedText = completeText
            }
            else if nearby.isOpen?.description == "true"
            {
                let imageAttachment =  NSTextAttachment()
                imageAttachment.image = UIImage(named:"openmorta")
                //Set bound to reposition
                let imageOffsetY:CGFloat = -5.0;
                imageAttachment.bounds = CGRect(x: 0, y: imageOffsetY, width: imageAttachment.image!.size.width, height: imageAttachment.image!.size.height)
                //Create string with attachment
                let attachmentString = NSAttributedString(attachment: imageAttachment)
                //Initialize mutable string
                let completeText = NSMutableAttributedString(string: nearby.name!+"  ")
                //Add image to mutable string
                completeText.append(attachmentString)
                //Add your text to mutable string
                let  textAfterIcon = NSMutableAttributedString(string: "")
                completeText.append(textAfterIcon)
                Name.textAlignment = .center;
                Name.attributedText = completeText
            }else{
                Name.text = nearby.name
            }
            let result: Double = Double(nearby.distance!) / Double(1609.344)
            let text = String(format: "%.2f", arguments: [result])
            let imageAttachmentad =  NSTextAttachment()
            imageAttachmentad.image = UIImage(named:"distancemorta")
            //Set bound to reposition
            let imageOffsetYad:CGFloat = -5.0;
            imageAttachmentad.bounds = CGRect(x: 0, y: imageOffsetYad, width: imageAttachmentad.image!.size.width, height: imageAttachmentad.image!.size.height)
            //Create string with attachment
            let attachmentStringad = NSAttributedString(attachment: imageAttachmentad)
            //Initialize mutable string
            let completeTextad = NSMutableAttributedString(string: "")
            //Add image to mutable string
            completeTextad.append(attachmentStringad)
            //Add your text to mutable string
            let  textAfterIconad = NSMutableAttributedString(string: "  "+nearby.name!+" is \(text) mi away")
            completeTextad.append(textAfterIconad)
            addrese.textAlignment = .center;
            addrese.attributedText = completeTextad
            tips.text = nearby.tips
            
            let imageAttachmenta =  NSTextAttachment()
            imageAttachmenta.image = UIImage(named:"favmorta")
            //Set bound to reposition
            let imageOffsetYa:CGFloat = -5.0;
            imageAttachmenta.bounds = CGRect(x: 0, y: imageOffsetYa, width: imageAttachmenta.image!.size.width, height: imageAttachmenta.image!.size.height)
            //Create string with attachment
            let attachmentStringa = NSAttributedString(attachment: imageAttachmenta)
            //Initialize mutable string
            let completeTexta = NSMutableAttributedString(string: "")
            //Add image to mutable string
            completeTexta.append(attachmentStringa)
            //Add your text to mutable string
            let ratingt = String(format: "%.1f", arguments: [nearby.rating!])
            
            let  textAfterIcona = NSMutableAttributedString(string: "  Rated \(ratingt) out of 10")
            completeTexta.append(textAfterIcona)
            Distance.textAlignment = .center;
            Distance.attributedText = completeTexta
        }else{
            Image.isHidden = true
            Distance.isHidden = true
            Name.isHidden = true
            Categorie.isHidden = true
            addrese.isHidden = true
            tips.isHidden = true
            imagemap.isHidden = true
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
                    let storyBoard : UIStoryboard = UIStoryboard(name: "Main", bundle:nil)
                    
                    let nearbylist = storyBoard.instantiateViewController(withIdentifier: "nearbylist") as! NearbyThingsViewController
                    
                    self.show(nearbylist, sender:true)
                }
            }
            
        }
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    override func viewDidAppear(_ animated: Bool) {
        layoutFAB()
    }
    func UIColorFromRGB(rgbValue: UInt) -> UIColor {
        return UIColor(
            red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
            green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
            blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
            alpha: CGFloat(1.0)
        )
    }
    func layoutFAB() {
        let fab = KCFloatingActionButton()
        let item = KCFloatingActionButtonItem()
        fab.buttonColor = UIColor(red: 0/255, green: 154/255, blue: 154/255, alpha: 0.9)
        
        item.circleShadowColor = UIColor.red
        item.titleShadowColor = UIColor(red: 188/255, green: 46/255, blue: 35/255, alpha: 0.5)
        item.title = "Custom item"
        item.handler = { item in
            
        }
        
        fab.addItem("Save Place", icon: UIImage(named: "saved places")) { item in
            let alertController = UIAlertController(title: self.nearby.name, message: "Add title to your location", preferredStyle: .alert)
            
            let saveAction = UIAlertAction(title: "Save", style: .default, handler: {
                alert -> Void in
                
                let firstTextField = alertController.textFields![0] as UITextField
                print(firstTextField.text!)
                if(firstTextField.text! != ""){
                    let name = (self.nearby.name)!
                    let cat = (self.nearby.categorie)!
                    let lat = (self.nearby.lat)!
                    let lng = (self.nearby.lng)!
                    let urlPath = URL(string: NavigatorData.URL+"/api/save")
                    let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec ]
                    let parameters = [
                        "title" : firstTextField.text!,
                        "name" : name,
                        "categorie" : cat,
                        "lat" : lat,
                        "log" : lng] as [String : Any]
                    
                    Alamofire.request(urlPath!, method: .post,parameters: parameters,encoding: JSONEncoding.default,headers: Auth_header ).responseJSON { responsep in
                        if(responsep.result.isSuccess){
                            // let userm = UserModel()
                            let dictp = responsep.result.value! as? Dictionary<String, AnyObject>
                            
                            let success = dictp?["success"]! as! NSNumber
                            
                            if(success==1){
                                /*let alert = UIAlertController(title: firstTextField.text!, message: "This place add to your saved places", preferredStyle: UIAlertControllerStyle.alert)
                                 alert.addAction(UIAlertAction(title: "ok", style: UIAlertActionStyle.default, handler: nil))
                                 self.present(alert, animated: true, completion: nil)*/
                                self.showToast(message: "This place add to your saved places")
                            }else{
                                self.showToast(message: "You already saved this place")
                            }
                        }else if(responsep.result.isFailure){
                            self.showToast(message: "Something wrong")
                        }
                        
                    }
                    
                }else{
                    self.showToast(message: "Set a title")
                }
            })
            
            let cancelAction = UIAlertAction(title: "Cancel", style: .default, handler: {
                (action : UIAlertAction!) -> Void in
                
            })
            alertController.addTextField { (textField : UITextField!) -> Void in
                textField.placeholder = "Enter Title"
            }
            
            
            alertController.addAction(saveAction)
            alertController.addAction(cancelAction)
            
            self.present(alertController, animated: true, completion: nil)
        }
        fab.addItem(nearby.categorie!, icon: UIImage(named: "route.png")) { item in
            let storyBoard : UIStoryboard = UIStoryboard(name: "Main", bundle:nil)
            
            let MapViewController = storyBoard.instantiateViewController(withIdentifier: "googlemapnearby") as! GoogleMapNearbyViewController
            MapViewController.nearby = self.nearby
            
            self.show(MapViewController, sender:true)
            
        }
        //fab.addItem(item: item)
        fab.fabDelegate = self
        
        self.view.addSubview(fab)
        
    }
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.endEditing(true)
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

