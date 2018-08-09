//
//  AddAgenceViewController.swift
//  TravelPlanner
//
//  Created by Ahmed Mohamed on 06/12/2017.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//

import UIKit
import Alamofire
class AddAgenceViewController: UIViewController ,UIImagePickerControllerDelegate,UINavigationControllerDelegate{

    @IBOutlet weak var nom: UITextField!
    @IBOutlet weak var imggggg: UIImageView!
    @IBOutlet weak var address: UITextField!
    @IBOutlet weak var tel: UITextField!
    @IBOutlet weak var validate: UIButton!
    @IBOutlet weak var email: UITextField!
    var img: UIImage = UIImage()
    var url:NSURL = NSURL()
    var test = 0
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.endEditing(true)
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        let color = UIColor.white
        nom.attributedPlaceholder = NSAttributedString(string: nom.placeholder!, attributes: [NSForegroundColorAttributeName : color])
        email.attributedPlaceholder = NSAttributedString(string: email.placeholder!, attributes: [NSForegroundColorAttributeName : color])
        address.attributedPlaceholder = NSAttributedString(string: address.placeholder!, attributes: [NSForegroundColorAttributeName : color])
        tel.attributedPlaceholder = NSAttributedString(string: tel.placeholder!, attributes: [NSForegroundColorAttributeName : color])
        
        validate.layer.cornerRadius = 13
        email.layer.cornerRadius = 13
        nom.layer.cornerRadius = 13
        address.layer.cornerRadius = 13
        tel.layer.cornerRadius = 13
        email.leftViewMode = UITextFieldViewMode.always
        
        let imageView = UIImageView(frame: CGRect(x: 0, y: 0, width: 20, height: 20))
        let image = UIImage(named: "mail.png")
        
        imageView.image = image
        email.leftView = imageView
        
        nom.leftViewMode = UITextFieldViewMode.always
        
        let imageViewp = UIImageView(frame: CGRect(x: 0, y: 0, width: 20, height: 20))
        let imagep = UIImage(named: "log.png")
        imageViewp.image = imagep
        nom.leftView = imageViewp
        
        address.leftViewMode = UITextFieldViewMode.always
        
        let imageViewpa = UIImageView(frame: CGRect(x: 0, y: 0, width: 20, height: 20))
        let imagepa = UIImage(named: "agencelocation.png")
        imageViewpa.image = imagepa
        address.leftView = imageViewpa
        
        tel.leftViewMode = UITextFieldViewMode.always
        
        let imageViewn = UIImageView(frame: CGRect(x: 0, y: 0, width: 20, height: 20))
        let imagen = UIImage(named: "telephone.png")
        imageViewn.image = imagen
        tel.leftView = imageViewn
        
        
        email.keyboardType = UIKeyboardType.emailAddress
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    
    @IBAction func addimg(_ sender: Any) {
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
        img = image
        test = 1
        
        
        picker.dismiss(animated: true, completion: nil)
    }

    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        picker.dismiss(animated: true, completion: nil)
    }
    func isValidEmail(testStr:String) -> Bool {
        let emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
        
        let emailTest = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
        return emailTest.evaluate(with: testStr)
    }
    @IBAction func validation(_ sender: Any) {
        
        if(test == 1 && nom.text != "" && tel.text != "" && address.text != "" && (isValidEmail(testStr: email.text!)==true)){
            let imageName         = url.lastPathComponent
            
            let jpegCompressionQuality: CGFloat = 0.9 // Set this to whatever suits your purpose
            if let base64String = UIImageJPEGRepresentation(img, jpegCompressionQuality)?.base64EncodedString() {
                let urlPath = URL(string: NavigatorData.URL+"/api/addagenceios")
                
               
                let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec]
                let parameters = [
                    "file" :base64String,
                    "nom" : nom.text as Any ,
                    "num_tel" : tel.text as Any ,
                    "logo" : imageName as Any ,
                    "adresse" : address.text as Any ,
                    "email" : email.text as Any ] as [String : Any]
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
           _ = SweetAlert().showAlert("Error!", subTitle: "Fill out all inputs!", style: AlertStyle.error)
        }
     
        
 
        
    }


}
