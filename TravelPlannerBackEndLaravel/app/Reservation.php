<?php

namespace App;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Notifications\Notifiable;

class Reservation extends Model
{
     use Notifiable;

    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = [
        'date','etat','user_id','pack_id',
    ];
    public function pack()
    {
        return $this->belongsTo('App\Pack');
    }

     public function user()
    {
        return $this->belongsTo('App\User');
    }
}