//
//  ProfileViewController.swift
//  TravelPlanner
//
//  Created by mortadha on 29/11/2017.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//

import UIKit
import Alamofire
class ProfileViewController: UIViewController, UIImagePickerControllerDelegate,UINavigationControllerDelegate {
    
    @IBOutlet var viewcont: UIView!
    @IBOutlet weak var iconpassword: UIImageView!
    @IBOutlet weak var ecitsave: UIBarButtonItem!
    @IBOutlet weak var nom: UITextField!
    @IBOutlet weak var numtel: UITextField!
    @IBOutlet weak var email: UITextField!
    
    
    
    @IBOutlet weak var background: UIImageView!
    
    
    @IBOutlet weak var password: UITextField!
    @IBOutlet weak var prenom: UITextField!
    @IBOutlet weak var username: UILabel!
    var isEdit = false
    var img: UIImage = UIImage()
    var url:NSURL? = NSURL()
    @IBOutlet weak var load: UIActivityIndicatorView!
    @IBOutlet weak var image: UIImageView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        image.layer.borderWidth = 1
        image.layer.masksToBounds = false
        
        image.layer.cornerRadius = image.frame.height/2
        image.clipsToBounds = true
        
        image.layer.borderColor = UIColor.white.cgColor
        if Connectivity.isConnectedToInternet() {
            ecitsave.image = #imageLiteral(resourceName: "edit1")
            nom.isUserInteractionEnabled = false
            prenom.isUserInteractionEnabled = false
            email.isUserInteractionEnabled = false
            numtel.isUserInteractionEnabled = false
            password.isHidden = true
            iconpassword.isHidden = true
            load.isHidden = false
            load.startAnimating()
            /*image.layer.masksToBounds = false
             image.layer.borderColor = UIColor.black.cgColor
             image.layer.cornerRadius = image.frame.height/2*/
            image.clipsToBounds = true
            if (SignUpViewController.connecteduser.image != nil) {
                let urlPath = URL(string: NavigatorData.URL+"/api/getphoto/\(SignUpViewController.connecteduser.idUser!)")
                
                let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec ]
                let parameters = [:] as [String : Any]
                Alamofire.request(urlPath!, method: .post,parameters: parameters,encoding: JSONEncoding.default,headers: Auth_header ).responseJSON { responsep in
                    if(responsep.result.isSuccess){
                        
                        let dictp = responsep.result.value! as? Dictionary<String, AnyObject>
                        
                        let photo = dictp?["photo"] as? String
                        
                        if let decodedData = Data(base64Encoded: photo!, options: .ignoreUnknownCharacters) {
                            let image = UIImage(data: decodedData)
                            self.load.isHidden = true
                            self.image.image = image
                        }
                    }else if(responsep.result.isFailure){
                    }
                }
                
            }else{
                self.load.isHidden = true
            }
            
            
            // Do any additional setup after loading the view.
            email.text = SignUpViewController.connecteduser.email
            username.text = SignUpViewController.connecteduser.username
            numtel.text = SignUpViewController.connecteduser.numtel
            if (SignUpViewController.connecteduser.nom != nil){
                nom.text = SignUpViewController.connecteduser.nom!
            }
            if (SignUpViewController.connecteduser.prenom != nil){
                prenom.text = SignUpViewController.connecteduser.prenom!
            }
            
        }else{
            nom.isHidden = true
            prenom.isHidden = true
            numtel.isHighlighted = true
            email.isHidden = true
            username.isHidden = true
            password.isHidden = true
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
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func edit(_ sender: Any) {
        if ecitsave.image == #imageLiteral(resourceName: "edit1") {
            ecitsave.image = #imageLiteral(resourceName: "save")
            let tapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(imageTapped(tapGestureRecognizer:)))
            image.isUserInteractionEnabled = true
            image.addGestureRecognizer(tapGestureRecognizer)
            
            nom.isUserInteractionEnabled = true
            prenom.isUserInteractionEnabled = true
            email.isUserInteractionEnabled = true
            numtel.isUserInteractionEnabled = true
            password.isHidden = false
            iconpassword.isHidden = false
        }else{
            if (!isValidEmail(testStr: email.text!)) {
                showToast(message: "Email is required")
            }else if(((numtel.text!).isEmpty) || ((numtel.text?.characters.count)! != 8)){
                showToast(message: "Number is required")
            }else if(((password.text!).isEmpty) || ((password.text?.characters.count)! < 6)){
                showToast(message: "password is required")
            }else{
                ecitsave.image = #imageLiteral(resourceName: "edit1")
                let imageName         = url?.lastPathComponent
                if isEdit {
                    let urlPath = URL(string: NavigatorData.URL+"/api/update/\(SignUpViewController.connecteduser.idUser!)")
                    let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec ]
                    let parameters = [
                        "nom" : nom.text!,
                        "prenom" : prenom.text!,
                        "email" : email.text!,
                        "image" : "",
                        "password" : password.text!,
                        "numtel" : Int(numtel.text!)!] as [String : Any]
                    Alamofire.request(urlPath!, method: .post,parameters: parameters,encoding: JSONEncoding.default,headers: Auth_header ).responseJSON { responsep in
                        if(responsep.result.isSuccess){
                            self.showToast(message: "Edit Successful")
                            let dictp = responsep.result.value! as? Dictionary<String, AnyObject>
                            let user = dictp?["user"]! as? Dictionary<String, AnyObject>
                            let nomm = user?["nom"] as? String
                            let prenomm = user?["prenom"] as? String
                            let username = user?["username"] as? String
                            let role = user?["role"] as? String
                            let image = user?["image"] as? String
                            let numtelm = user?["numtel"] as? Int
                            let emailm = user?["email"] as? String
                            let id = user?["id"] as? Int
                            SignUpViewController.connecteduser.idUser = id
                            SignUpViewController.connecteduser.nom = nomm
                            SignUpViewController.connecteduser.prenom = prenomm
                            SignUpViewController.connecteduser.username = username
                            SignUpViewController.connecteduser.role = role
                            SignUpViewController.connecteduser.email = emailm
                            SignUpViewController.connecteduser.numtel = String(describing: numtelm!)
                            SignUpViewController.connecteduser.image = image
                            self.image.isUserInteractionEnabled = false
                            self.nom.isUserInteractionEnabled = false
                            self.prenom.isUserInteractionEnabled = false
                            self.email.isUserInteractionEnabled = false
                            self.numtel.isUserInteractionEnabled = false
                            self.password.isHidden = true
                            self.iconpassword.isHidden = true
                            
                        }else if(responsep.result.isFailure){
                            self.showToast(message: "Something wrong")
                            print(responsep.result.error as Any)
                        }
                    }
                    
                }else{
                    if imageName != nil {
                        let _:NSData = UIImagePNGRepresentation(img)! as NSData
                        
                        let jpegCompressionQuality: CGFloat = 0.9 // Set this to whatever suits your purpose
                        if let base64String = UIImageJPEGRepresentation(img, jpegCompressionQuality)?.base64EncodedString() {
                            let urlPath = URL(string: NavigatorData.URL+"/api/update/\(SignUpViewController.connecteduser.idUser!)")
                            print("okok")
                            let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec ]
                            let parameters = [
                                "file" :base64String,
                                "nom" : nom.text!,
                                "prenom" : prenom.text!,
                                "email" : email.text!,
                                "image" : imageName!,
                                "password" : password.text!,
                                "numtel" : Int(numtel.text!)!] as [String : Any]
                            Alamofire.request(urlPath!, method: .post,parameters: parameters,encoding: JSONEncoding.default,headers: Auth_header ).responseJSON { responsep in
                                if(responsep.result.isSuccess){
                                    self.showToast(message: "Edit Successful")
                                    let dictp = responsep.result.value! as? Dictionary<String, AnyObject>
                                    let user = dictp?["user"]! as? Dictionary<String, AnyObject>
                                    let nomm = user?["nom"] as? String
                                    let prenomm = user?["prenom"] as? String
                                    let username = user?["username"] as? String
                                    let image = user?["image"] as? String
                                    
                                    let role = user?["role"] as? String
                                    let numtelm = user?["numtel"] as? Int
                                    let emailm = user?["email"] as? String
                                    let id = user?["id"] as? Int
                                    SignUpViewController.connecteduser.idUser = id
                                    SignUpViewController.connecteduser.nom = nomm
                                    SignUpViewController.connecteduser.prenom = prenomm
                                    SignUpViewController.connecteduser.username = username
                                    SignUpViewController.connecteduser.role = role
                                    SignUpViewController.connecteduser.email = emailm
                                    SignUpViewController.connecteduser.numtel = String(describing: numtelm!)
                                    SignUpViewController.connecteduser.image = image
                                    
                                    self.image.isUserInteractionEnabled = false
                                    self.nom.isUserInteractionEnabled = false
                                    self.prenom.isUserInteractionEnabled = false
                                    self.email.isUserInteractionEnabled = false
                                    self.numtel.isUserInteractionEnabled = false
                                    self.password.isHidden = true
                                    self.iconpassword.isHidden = true
                                    
                                }else if(responsep.result.isFailure){
                                    self.showToast(message: "Something wrong")
                                    print("Something wrong")
                                }
                            }
                            
                        }
                    }else{
                        let urlPath = URL(string: NavigatorData.URL+"/api/update/\(SignUpViewController.connecteduser.idUser!)")
                        let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec ]
                        let parameters = [
                            "nom" : nom.text!,
                            "prenom" : prenom.text!,
                            "email" : email.text!,
                            "password" : password.text!,
                            "numtel" : Int(numtel.text!)!] as [String : Any]
                        Alamofire.request(urlPath!, method: .post,parameters: parameters,encoding: JSONEncoding.default,headers: Auth_header ).responseJSON { responsep in
                            if(responsep.result.isSuccess){
                                self.showToast(message: "Edit Successful")
                                let dictp = responsep.result.value! as? Dictionary<String, AnyObject>
                                let user = dictp?["user"]! as? Dictionary<String, AnyObject>
                                let nomm = user?["nom"] as? String
                                let prenomm = user?["prenom"] as? String
                                let username = user?["username"] as? String
                                let role = user?["role"] as? String
                                let image = user?["image"] as? String
                                let numtelm = user?["numtel"] as? Int
                                let emailm = user?["email"] as? String
                                let id = user?["id"] as? Int
                                SignUpViewController.connecteduser.idUser = id
                                SignUpViewController.connecteduser.nom = nomm
                                SignUpViewController.connecteduser.prenom = prenomm
                                SignUpViewController.connecteduser.username = username
                                SignUpViewController.connecteduser.role = role
                                SignUpViewController.connecteduser.image = image
                                SignUpViewController.connecteduser.email = emailm
                                SignUpViewController.connecteduser.numtel = String(describing: numtelm!)
                                self.image.isUserInteractionEnabled = false
                                self.nom.isUserInteractionEnabled = false
                                self.prenom.isUserInteractionEnabled = false
                                self.email.isUserInteractionEnabled = false
                                self.numtel.isUserInteractionEnabled = false
                                self.password.isHidden = true
                                self.iconpassword.isHidden = true
                                
                            }else if(responsep.result.isFailure){
                                self.showToast(message: "Something wrong")
                                print(responsep.result.error as Any)
                            }
                        }
                    }
                    
                    
                }
            }
        }
        
        
    }
    func imageTapped(tapGestureRecognizer: UITapGestureRecognizer)
    {
        //  let tappedImage = tapGestureRecognizer.view as! UIImageView
        
        let imagepickercontroller = UIImagePickerController()
        imagepickercontroller.delegate = self
        
        let actionSheet = UIAlertController(title: "photo source", message: "choose a source", preferredStyle: .actionSheet)
        /*actionSheet.addAction(UIAlertAction(title: "camera", style: .default, handler: { (action:UIAlertAction) in
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
        actionSheet.addAction(UIAlertAction(title: "delete", style: .default, handler: {
            (action:UIAlertAction) in
            SweetAlert().showAlert("Are you sure?", subTitle: "You picture will be permanently deleted!", style: AlertStyle.warning, buttonTitle:"Cancel", buttonColor:self.UIColorFromRGB(rgbValue: 0xD0D0D0) , otherButtonTitle:  "Yes, delete it!", otherButtonColor: self.UIColorFromRGB(rgbValue: 0xDD6B55)) { (isOtherButton) -> Void in
                if isOtherButton == true {
                    
                }
                else {
                    _ = SweetAlert().showAlert("Deleted!", subTitle: "Your picture has been deleted!", style: AlertStyle.success)
                    self.image.image = #imageLiteral(resourceName: "user")
                    self.url = nil
                    self.isEdit = true
                }
            }
            
        }))
        actionSheet.addAction(UIAlertAction(title: "cancel", style: .cancel, handler: nil))
        self.present(actionSheet, animated: true, completion: nil)
        
        
    }
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
        let image = info[UIImagePickerControllerOriginalImage] as! UIImage
        let imageUrl          = info[UIImagePickerControllerReferenceURL] as! NSURL
        url = imageUrl
        isEdit = false
        img = image
        picker.dismiss(animated: true, completion: nil)
        self.image.image = self.img
        showToast(message: "image uploaded")
    }
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        picker.dismiss(animated: true, completion: nil)
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
    func isValidEmail(testStr:String) -> Bool {
        let emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
        
        let emailTest = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
        return emailTest.evaluate(with: testStr)
    }
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.endEditing(true)
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

