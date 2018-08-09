//
//  ModifMyAgencyViewController.swift
//  TravelPlanner
//
//  Created by Ahmed Mohamed on 09/01/2018.
//  Copyright © 2018 AhmedMohamed. All rights reserved.
//

import UIKit
import Alamofire
class ModifMyAgencyViewController: UIViewController ,UIImagePickerControllerDelegate, UINavigationControllerDelegate{

    
    static var imgm = UIImage()
     static var nomm: String?
    static var addressm: String?
    static var idag: String?
    static var mailm: String?
    static var numtelm: String?
    static var faxm: String?
    var test = 0
    var img1: UIImage = UIImage()
    var url:NSURL = NSURL()
    
    
    
    @IBOutlet weak var fifth: UITextField!
    @IBOutlet weak var forth: UITextField!
    @IBOutlet weak var third: UITextField!
    @IBOutlet weak var second: UITextField!
    @IBOutlet weak var first: UITextField!
    @IBOutlet weak var img: UIImageView!
    override func viewDidLoad() {
        super.viewDidLoad()
        first.text = ModifMyAgencyViewController.nomm
        second.text = ModifMyAgencyViewController.mailm
        third.text = ModifMyAgencyViewController.addressm
        forth.text = ModifMyAgencyViewController.numtelm
        if(ModifMyAgencyViewController.faxm == "Fax not set yet"){
            fifth.placeholder = ModifMyAgencyViewController.faxm
        }else{
            fifth.text = ModifMyAgencyViewController.faxm
        }
        
        
        
        img.layer.borderWidth = 1
        img.layer.masksToBounds = false
        
        img.layer.cornerRadius = img.frame.height/2
        img.clipsToBounds = true
        
        img.layer.borderColor = UIColor.white.cgColor
        img.image = ModifMyAgencyViewController.imgm
        if Connectivity.isConnectedToInternet() == true {
            
        
            
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
    @IBAction func selectimg(_ sender: Any) {
        let imagepickercontroller = UIImagePickerController()
        imagepickercontroller.delegate = self
        
        let actionSheet = UIAlertController(title: "photo source", message: "choose a source", preferredStyle: .actionSheet)
        /*
        actionSheet.addAction(UIAlertAction(title: "camera", style: .default, handler: { (action:UIAlertAction) in
            if UIImagePickerController.isSourceTypeAvailable(.camera){
                imagepickercontroller.sourceType = .camera
                self.present(imagepickercontroller,animated: true,completion: nil)
            }else{
                print("camera not avaibale")
            }
            
            
        }))*/
        actionSheet.addAction(UIAlertAction(title: "photo library", style: .default, handler: { (action:UIAlertAction) in
            imagepickercontroller.sourceType = .photoLibrary
            self.present(imagepickercontroller,animated: true,completion: nil)
            
        }))
        actionSheet.addAction(UIAlertAction(title: "cancel", style: .cancel, handler: nil))
        self.present(actionSheet, animated: true, completion: nil)
        
        
    }

    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
        let image = info[UIImagePickerControllerOriginalImage] as! UIImage
        let imageUrl          = info[UIImagePickerControllerReferenceURL] as! NSURL
        url = imageUrl
        img1 = image
        self.img.image = img1
        self.test = 1
        picker.dismiss(animated: true, completion: nil)
    }
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        picker.dismiss(animated: true, completion: nil)
    }
    
    
    @IBAction func saveagency(_ sender: Any) {
        
        if( (self.first.text != "") && (self.second.text != "") && (self.third.text != "") && (self.forth.text != "")){
            if(test == 1){
                let imageName         = url.lastPathComponent
               
                let jpegCompressionQuality: CGFloat = 0.9 // Set this to whatever suits your purpose
                if let base64String = UIImageJPEGRepresentation(img1, jpegCompressionQuality)?.base64EncodedString() {
                    let urlPath = URL(string: NavigatorData.URL+"/api/modifagence")
                    
             
                    let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec]
                    let parameters = [
                        "file" :base64String,
                        "nom" : first.text as Any ,
                        "num_tel" : forth.text as Any ,
                        "logo" : imageName as Any ,
                        "adresse" : third.text as Any ,
                        "id" : ModifMyAgencyViewController.idag as Any ,
                        "fax" : fifth.text as Any ,
                        "email" : second.text as Any ] as [String : Any]
                    Alamofire.request(urlPath!, method: .post,parameters: parameters,encoding: JSONEncoding.default,headers: Auth_header ).responseJSON { responsep in
                        if(responsep.result.isSuccess){
                            
                            let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
                            let newViewController = storyBoard.instantiateViewController(withIdentifier: "agenthome") as! UITabBarController
                            self.present(newViewController, animated: true, completion: nil)
                            
                            
                        }else if(responsep.result.isFailure){
                            print(responsep.result)
                        }
                        
                    }
                    
                }
            }else{
              
                
                let jpegCompressionQuality: CGFloat = 0.9 // Set this to whatever suits your purpose
                if let base64String = UIImageJPEGRepresentation(ModifMyAgencyViewController.imgm, jpegCompressionQuality)?.base64EncodedString() {
                    let urlPath = URL(string: NavigatorData.URL+"/api/modifagence")
                    
                    
                    let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec]
                    let parameters = [
                        "file" :base64String,
                        "nom" : first.text as Any ,
                        "num_tel" : forth.text as Any ,
                        
                        "adresse" : third.text as Any ,
                        "id" : ModifMyAgencyViewController.idag as Any ,
                        "fax" : fifth.text as Any ,
                        "email" : second.text as Any ] as [String : Any]
                    Alamofire.request(urlPath!, method: .post,parameters: parameters,encoding: JSONEncoding.default,headers: Auth_header ).responseJSON { responsep in
                        if(responsep.result.isSuccess){
                            let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
                            let newViewController = storyBoard.instantiateViewController(withIdentifier: "agenthome") as! UITabBarController
                            self.present(newViewController, animated: true, completion: nil)
                            
                            
                            
                        }else if(responsep.result.isFailure){
                            print(responsep.result)
                        }
                        
                    }
                    
                }
            }
            
            
           
        }else{
            showToast(message: "fill inputs")
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
