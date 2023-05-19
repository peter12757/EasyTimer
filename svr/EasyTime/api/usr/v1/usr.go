package v1

import "github.com/gogf/gf/v2/frame/g"

type UsrInfoReq struct {
	g.Meta    `path:"/user/login" method:"get" tags:"UserServices" summary:"login or sign up"`
	Passport  string `v:"required"`
	Password  string `v:"required"`
	LoginType int    `v:"required"`
} 


type UsrInfoRes struct {
	// Code int
	// Msg  string
}
 