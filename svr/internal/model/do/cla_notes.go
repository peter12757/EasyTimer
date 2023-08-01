// =================================================================================
// Code generated by GoFrame CLI tool. DO NOT EDIT.
// =================================================================================

package do

import (
	"github.com/gogf/gf/v2/frame/g"
	"github.com/gogf/gf/v2/os/gtime"
)

// ClaNotes is the golang structure of table Cla_Notes for DAO operations like Where/Data.
type ClaNotes struct {
	g.Meta `orm:"table:Cla_Notes, do:true"`
	Date   *gtime.Time // 日期
	Group  interface{} // 分组
	Desc   interface{} // 描述
	Id     interface{} // ID
}