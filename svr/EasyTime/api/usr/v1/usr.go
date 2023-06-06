package v1

import "github.com/gogf/gf/v2/frame/g"

type SignUpReq struct {
	g.Meta    `path:"/user/signup" method:"get" tags:"UserServices" summary:"sign up"`
	Passport  string `v:"required"`
	Password  string `v:"required"`
	NickName  string `v:"required"`
	Phone     string `v:"required"`
	LoginType int    `v:"required"`
}

type SignUpRes struct {
	Code int    `v:"required"`
	Msg  string `v:"required"`
}

type SignInReq struct {
	g.Meta    `path:"/user/signin" method:"get" tags:"UserServices" summary:"sign up"`
	Passport  string `v:"required"`
	Password  string `v:"required"`
	LoginType int    `v:"required"`
}

type SignInRes struct {
}
