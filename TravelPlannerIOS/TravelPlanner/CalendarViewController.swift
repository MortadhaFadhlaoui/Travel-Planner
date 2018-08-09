//
//  CalendarViewController.swift
//  TravelPlanner
//
//  Created by ESPRIT on 13/12/2017.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//
import Foundation
import UIKit
import YMCalendar
class CalendarViewController: UIViewController {

    
    static var list = [String]()
    static var list1 = [String]()
   
    @IBOutlet weak var vieww: YMCalendarView!
    @IBOutlet weak var menu: YMCalendarWeekBarView!
    let calendar = Calendar.current
    
    let MyCustomEventViewIdentifier = "MyCustomEventViewIdentifier"
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        menu.appearance = self
        menu.calendar = calendar
        menu.gradientColors = [.sienna, .violetred]
        menu.gradientStartPoint = CGPoint(x: 0.0, y: 0.5)
        menu.gradientEndPoint   = CGPoint(x: 1.0, y: 0.5)
        
        vieww.appearance = self
        vieww.delegate   = self
        vieww.dataSource = self
        vieww.calendar   = calendar
        vieww.isPagingEnabled = true
        vieww.scrollDirection = .horizontal
        vieww.selectAnimation = .fade
        vieww.eventViewHeight = 22
        vieww.gradientColors  = [.sienna, .violetred]
        vieww.gradientStartPoint = CGPoint(x: 0.0, y: 0.5)
        vieww.gradientEndPoint   = CGPoint(x: 1.0, y: 0.5)
        vieww.registerClass(MyCustomEventView.self, forEventCellReuseIdentifier: MyCustomEventViewIdentifier)
    }
}

extension CalendarViewController: YMCalendarWeekBarAppearance {
    func weekBarHorizontalGridColor(in view: YMCalendarWeekBarView) -> UIColor {
        return .clear
    }
    
    func weekBarVerticalGridColor(in view: YMCalendarWeekBarView) -> UIColor {
        return .white
    }
    
    func calendarWeekBarView(_ view: YMCalendarWeekBarView, textColorAtWeekday weekday: Int) -> UIColor {
        return .white
    }
}

extension CalendarViewController: YMCalendarAppearance {
    func horizontalGridColor(in view: YMCalendarView) -> UIColor {
        return .white
    }
    
    func verticalGridColor(in view: YMCalendarView) -> UIColor {
        return .clear
    }
    
    func calendarViewAppearance(_ view: YMCalendarView, dayLabelTextColorAtDate date: Date) -> UIColor {
        return .white
    }
    
    func calendarViewAppearance(_ view: YMCalendarView, dayLabelSelectedTextColorAtDate date: Date) -> UIColor {
        return .sienna
    }
    
    func calendarViewAppearance(_ view: YMCalendarView, dayLabelSelectedBackgroundColorAtDate date: Date) -> UIColor {
        return .white
    }
}

extension CalendarViewController: YMCalendarDelegate {
    func calendarView(_ view: YMCalendarView, didSelectDayCellAtDate date: Date) {
        let formatter = DateFormatter()
        formatter.dateFormat = "YYYY-MM-dd"
        navigationItem.title = formatter.string(from: date)
    }
}

extension CalendarViewController: YMCalendarDataSource {
    func calendarView(_ view: YMCalendarView, numberOfEventsAtDate date: Date) -> Int {
        if calendar.isDateInToday(date) {
            return 1
        }
        
        
        
        
        for item in CalendarViewController.list {
            let js = item
            let js1 = js.substring(to:(js.index((js.startIndex), offsetBy: 10)))
            
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "YYYY-MM-dd" //Your date format
            dateFormatter.timeZone = TimeZone(abbreviation: "GMT+0:00") //Current time zone
            let date1 = dateFormatter.date(from: js1)
            let stringdate = dateFormatter.string(from: date1!)
            let stringdate1 = dateFormatter.string(from: date)
            let x1 = stringdate.substring(to:stringdate.index(stringdate.startIndex, offsetBy: 10))
            let x2 = stringdate1.substring(to:stringdate1.index(stringdate1.startIndex, offsetBy: 10))
            if (x1 == x2){
                return 1
            }
            
            
        }
        
        
        
        
       
        return 0
    }
    
    func calendarView(_ view: YMCalendarView, dateRangeForEventAtIndex index: Int, date: Date) -> DateRange? {
        if calendar.isDateInToday(date) {
            return DateRange(start: date, end: calendar.endOfDayForDate(date))
        }
        
        
        
        
        for item in CalendarViewController.list {
            let js = item
            let js1 = js.substring(to:(js.index((js.startIndex), offsetBy: 10)))
            
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "YYYY-MM-dd" //Your date format
            dateFormatter.timeZone = TimeZone(abbreviation: "GMT+0:00") //Current time zone
            let date1 = dateFormatter.date(from: js1)
            let stringdate = dateFormatter.string(from: date1!)
            let stringdate1 = dateFormatter.string(from: date)
            let x1 = stringdate.substring(to:stringdate.index(stringdate.startIndex, offsetBy: 10))
            let x2 = stringdate1.substring(to:stringdate1.index(stringdate1.startIndex, offsetBy: 10))
         
            
            if (x1 == x2){
                return DateRange(start: date, end: calendar.endOfDayForDate(date))
            }
            
            
        }

        return nil
    }
    
    func calendarView(_ view: YMCalendarView, eventViewForEventAtIndex index: Int, date: Date) -> YMEventView {
        guard let view = view.dequeueReusableCellWithIdentifier(MyCustomEventViewIdentifier, forEventAtIndex: index, date: date) as? MyCustomEventView else {
            fatalError()
        }
        var x = 0
        for item in CalendarViewController.list {
            let js = item
            let js1 = js.substring(to:(js.index((js.startIndex), offsetBy: 10)))
            
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "YYYY-MM-dd" //Your date format
            dateFormatter.timeZone = TimeZone(abbreviation: "GMT+0:00") //Current time zone
            let date1 = dateFormatter.date(from: js1)
            let stringdate = dateFormatter.string(from: date1!)
            let stringdate1 = dateFormatter.string(from: date)
            let x1 = stringdate.substring(to:stringdate.index(stringdate.startIndex, offsetBy: 10))
            let x2 = stringdate1.substring(to:stringdate1.index(stringdate1.startIndex, offsetBy: 10))
            
            
            if (x1 == x2){
                view.label.text = CalendarViewController.list1[x]
                view.label.textColor = .white
                view.backgroundColor = UIColor(white: 1.0, alpha: 0.25)
            }
            
            x = x+1
        }
        if calendar.isDateInToday(date) {
            view.label.text = "today"
            view.label.textColor = .white
            view.backgroundColor = UIColor(white: 1.0, alpha: 0.25)
        }

        
        return view
    }
}
