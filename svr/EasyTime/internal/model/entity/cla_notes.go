// =================================================================================
// Code generated by GoFrame CLI tool. DO NOT EDIT.
// =================================================================================

package entity

import (
	"github.com/gogf/gf/v2/os/gtime"
)

// ClaNotes is the golang structure for table Cla_Notes.
type ClaNotes struct {
	Date  *gtime.Time `json:"date"  ` // 日期
	Group string      `json:"group" ` // 分组
	Desc  string      `json:"desc"  ` // 描述
	Id    int         `json:"id"    ` // ID
}
