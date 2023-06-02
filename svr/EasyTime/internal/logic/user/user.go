package user

import (
	"context"

	"EasyTime/internal/dao"
	"EasyTime/internal/model"
	"EasyTime/internal/model/do"
	"EasyTime/internal/model/entity"
	"EasyTime/internal/service"

	"github.com/gogf/gf/v2/database/gdb"
	"github.com/gogf/gf/v2/errors/gerror"
)

type (
	sUser struct{}
)

func init() {
	service.RegisterUser(New())
}

func New() *sUser {
	return &sUser{}
}

func (s *sUser) Create(ctx context.Context, in model.UserCreateInput) (err error) {
	if in.NickName == "" {
		in.NickName = in.Passport
	}
	var aviliable bool

	aviliable, err = service.User().IsPassportAvilable(ctx, in.Passport)
	if err != nil {
		return err
	}
	if !aviliable {
		return gerror.Newf(`Passport "%s" is already registed!!!`, in.Passport)

	}
	return dao.User.Transaction(ctx, func(ctx context.Context, tx gdb.TX) error {
		_, err = dao.User.Ctx(ctx).Data(do.User{
			Passport: in.Passport,
			Password: in.Password,
			Nickname: in.NickName,
			Phone:    in.Phone,
		}).Insert()
		return err
	})
}

func (s *sUser) SignIn(ctx context.Context, in model.UserSignInput) (err error) {
	var user *entity.User
	err = dao.User.Ctx(ctx).Where(do.User{
		Passport: in.Passport,
		Password: in.Password,
	}).Scan(&user)
	if err != nil {

		return err
	}
	if user == nil {
		return gerror.New(`Passport or Password not correct`)
	}
	//session is online???
	return nil
}

func (s *sUser) IsPassportAvilable(ctx context.Context, passport string) (bool, error) {
	count, err := dao.User.Ctx(ctx).Where(do.User{
		Passport: passport,
	}).Count()
	if err != nil {
		return false, err
	}
	return count == 0, nil
}
