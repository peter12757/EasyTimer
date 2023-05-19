package user

import (
	v1 "EasyTime/api/usr/v1"
	"EasyTime/internal/model"
	"EasyTime/internal/service"
	"context"
)

type Controller struct{}

func New() *Controller {
	return &Controller{}
}

func (c *Controller) SignUp(ctx context.Context, req *v1.UsrInfoReq) (res *v1.UsrInfoRes, err error) {
	err = service.User().Create(ctx, model.UserCreateInput{
		Passport: req.Passport,
		Password: req.Password,
		NickName: req,
	}())

}
