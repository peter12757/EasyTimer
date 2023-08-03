package v1

import (
	"github.com/gogf/gf/v2/frame/g"
)

type RegisterReq struct {
	g.Meta    `path:"/user/signup" method:"get" tags:"UserServices" summary:"sign up"`
	Passport  string `v:"required"`
	Password  string `v:"required"`
	NickName  string `v:"required"`
	Phone     string `v:"required"`
	LoginType int    `v:"required"`
}

type RegisterRes struct {
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
	Code int    `v:"required"`
	Msg  string `v:"required"`
}

type ForgetPasswordReq struct {
	g.Meta     `path:"/user/forget" method:"get" tag:"UserServices" summary:"forget password"`
	ForgetType int    `v:"required"`
	KeyWord    string `v:"required"`
}

type ForgetPasswordRes struct {
	Code int    `v:"required"`
	Msg  string `v:"required"`
}
