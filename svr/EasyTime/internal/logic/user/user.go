package user

import (
	"context"

	"EasyTime/internal/model/entity"
)

func Create(ctx context.Context, in entity.User) (err *error) {
	if in.Nickname == "" {
		in.Nickname = in.
	}
}
