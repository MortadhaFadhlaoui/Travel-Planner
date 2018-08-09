//
//  MyCustomEventView.swift
//  TravelPlanner
//
//  Created by ESPRIT on 13/12/2017.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//


import Foundation
import UIKit
import YMCalendar

final class MyCustomEventView: YMEventView {
    
    let label = UILabel()
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        label.font = .systemFont(ofSize: 10.0)
        label.textAlignment = .center
        addSubview(label)
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        label.frame = bounds
    }
}
