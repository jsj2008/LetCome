require('UIColor');
defineClass('IndexViewController', {
            addDockItem: function() {
            var dock = self.valueForKey("_dock")     //get member variables
            dock.addItemWithIcon_selectedIcon_title_titleColor("tab_home", "tab_home_s", "大厅14", UIColor.colorWithRed_green_blue_alpha(251/255, 111/ 255, 33/ 255, 1));
            dock.addItemWithIcon_selectedIcon_title_titleColor("tab_strategy", "tab_strategy_s", "发策略13", UIColor.colorWithRed_green_blue_alpha(251/255, 111/ 255, 33/ 255, 1));
            dock.addItemWithIcon_selectedIcon_title_titleColor("tab_implement", "tab_implement_s", "盯执行12", UIColor.colorWithRed_green_blue_alpha(251/255, 111/ 255, 33/ 255, 1));
            dock.addItemWithIcon_selectedIcon_title_titleColor("tab_receivable", "tab_receivable_s", "收付款11", UIColor.colorWithRed_green_blue_alpha(251/255, 111/ 255, 33/ 255, 1));
            dock.addItemWithIcon_selectedIcon_title_titleColor("tab_mine", "tab_mine_s", "我10", UIColor.colorWithRed_green_blue_alpha(251/255, 111/ 255, 33/ 255, 1));
            },
            });