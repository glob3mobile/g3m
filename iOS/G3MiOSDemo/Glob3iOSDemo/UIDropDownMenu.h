//
//  UIDropDownMenu.m
//  DropDownMenu
//
//  Created on 30/03/2012.
//  Updated by Add Image on 17/01/2013.
//  Copyright (c) 2013 Add Image
//
//  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

//  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

//

#import <Foundation/Foundation.h>

@protocol UIDropDownMenuDelegate <NSObject>
@optional
- (void) DropDownMenuDidChange:(NSString *)identifier :(NSString *)ReturnValue;
@end

@interface UIDropDownMenu : NSObject <UITextFieldDelegate, UITableViewDelegate, UITableViewDataSource, UIGestureRecognizerDelegate>
{
    // general objects
    UITableView *dropdownTable;
    UIView *parentView;
    UITapGestureRecognizer *singleTapGestureRecogniser;
    NSString *identifiername;
    
    
    //Object which the menu is attached to
    NSObject *targetObject;
    
    // possible object types
    UITextField *selectedTextField;
    UIButton *selectedButton;
    
    // arrays
    NSMutableArray *titleArray;
    NSMutableArray *valueArray;
    
    // styling variables
    BOOL scaleToFitParent;
  BOOL menuFillParent;
    int menuWidth;
    int rowHeight;
    UIColor *textColor;
    UIColor *backgroundColor;
    UIColor *borderColor;
    
    // value to return when clicked
    NSString *selectedValue;

    
    id <UIDropDownMenuDelegate> delegate;
}


@property (retain, nonatomic) UITableView *dropdownTable;
@property (retain, nonatomic) UIView *parentView;
@property (retain, nonatomic) UITapGestureRecognizer *singleTapGestureRecogniser;
@property (retain, nonatomic) NSString *identifiername;
@property (retain, nonatomic) NSObject *targetObject;
@property (retain, nonatomic) UITextField *selectedTextField;
@property (retain, nonatomic) UIButton *selectedButton;
@property (retain, nonatomic) NSMutableArray *titleArray;
@property (retain, nonatomic) NSMutableArray *valueArray;
@property (retain, nonatomic) UIColor *textColor;
@property (retain, nonatomic) UIColor *backgroundColor;
@property (retain, nonatomic) UIColor *borderColor;
@property (nonatomic) BOOL scaleToFitParent;
@property (nonatomic) BOOL menuFillParent;
@property (nonatomic) int menuWidth;
@property (nonatomic) int rowHeight;
@property (retain, nonatomic) NSString *selectedValue;
 

@property (retain) id delegate;
@property (assign) id <UIDropDownMenuDelegate> DropDownMenuDelegate;



- (id) initWithIdentifier:(NSString *)identifier;
-(void)makeMenu:(NSObject *)targetObject targetView:(UIView *)tview;
-(void)selectedObjectClicked:(id)sender;
-(void)dismissMenu;
@end
